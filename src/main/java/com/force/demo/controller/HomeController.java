package com.force.demo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Checkin;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.force.demo.dao.NoteDao;
import com.force.demo.model.Note;

@Controller
public class HomeController {

	@Autowired
	private NoteDao noteDao;
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	public ModelAndView home(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		//TODO: fix exception handling
		String signed_request = req.getParameter("signed_request");
		
		if(signed_request == null) {
			return redirectForLogin();
		}
		
		System.out.println("signed request: " + signed_request);
		String[] elements = signed_request.split("\\.");
		
		if(elements.length < 2) {
			return redirectForLogin();
		}
		
		//TODO: verify checksum
		String payload = elements[1];
		System.out.println("payload: " + payload);
		Base64 decoder = new Base64(true);
		String data = new String(decoder.decode(payload.getBytes()));
		String oauthToken = getOAuthToken(data);

		if(oauthToken == null || "".equals(oauthToken)) {
			return redirectForLogin();
		}
		
		return renderMainPage(oauthToken);
	}
	
	@RequestMapping(value="/note/{profileId}/{placeId}", method=RequestMethod.POST)
	public ModelAndView saveNote(@PathVariable String profileId, @PathVariable String placeId, 
			HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		Note note = new Note();
		note.setPlaceId(placeId);
		note.setProfileId(profileId);
		note.setText("text");
		noteDao.saveNote(note);
		ModelAndView mv = new ModelAndView("canvas-social");
		return mv;
	}
	
	private ModelAndView redirectForLogin() {
		ModelAndView mv = new ModelAndView("canvas-social");
		mv.addObject("sendRedirect", true);
		return mv;
	}
	
	private ModelAndView renderMainPage(String oauthToken) {
		ModelAndView mv = new ModelAndView("canvas-social");
		
		mv.addObject("accessToken", oauthToken);
		
		mv.addObject("sendRedirect", false);					
		//mv.addObject("checkins", getCheckInInfo(req, getOAuthToken(data)));
		mv.addObject("checkInObjs", getCheckInObjects(oauthToken));
		mv.addObject("profileId", getProfileId(oauthToken));
		
		return mv;		
	}
	
	private String getOAuthToken(String data) throws ServletException {
		ObjectMapper mapper = new ObjectMapper();
		String oauthToken = null;
		try {
			JsonNode rootNode = mapper.readValue(data.getBytes(), JsonNode.class);
			if(rootNode.path("oauth_token") != null) {				
				oauthToken = rootNode.path("oauth_token").getTextValue();
			}
		} catch (JsonParseException e) {
			throw new ServletException(e);
		} catch (JsonMappingException e) {
			throw new ServletException(e);
		} catch (IOException e) {
			throw new ServletException(e);
		}
		
		return oauthToken;
	}
	
    private URL buildFBUrl(HttpServletRequest req, String target, String accessToken) throws MalformedURLException {
        return new URL("https://graph.facebook.com" + target + "?access_token=" + accessToken);
    }
    
    private String readApiData(URL apiUrl) {
        StringBuilder jsonReturn = new StringBuilder();
        BufferedReader in = null;
        
        try {
            URLConnection urlConn = apiUrl.openConnection();   
            in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            
            String inputLine;
            
            while((inputLine = in.readLine()) != null) {
                jsonReturn.append(inputLine);
            }             
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(in != null) {
                    in.close();                
                }   
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        
        return jsonReturn.toString(); 
    }
    
    private String getCheckInInfo(HttpServletRequest req, String token) {
    	URL restUrl;
    	try {
    		restUrl = buildFBUrl(req, "/me/checkins", token);
    		return readApiData(restUrl);
    	} catch (MalformedURLException e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    private List<Checkin> getCheckInObjects(String token) {
    	Facebook facebook = new FacebookTemplate(token);
    	List<Checkin> checkIns = facebook.placesOperations().getCheckins();
    	for (Checkin checkin : checkIns) {
    		System.out.println("Check In: " + checkin.getPlace().getName());
    	}
    	
    	return checkIns;
    }
    
    private String getProfileId(String token) {
    	Facebook facebook = new FacebookTemplate(token);
    	FacebookProfile profile = facebook.userOperations().getUserProfile();
    	return profile.getId();
    }
}
