package com.force.demo.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.social.facebook.api.Checkin;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

public class HomeServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String signed_request = req.getParameter("signed_request");
		
		if(signed_request != null) {
			System.out.println("signed request: " + signed_request);
			String[] elements = signed_request.split("\\.");
			
			if(elements.length > 1) {			
				String payload = elements[1];
				System.out.println("payload: " + payload);
				Base64 decoder = new Base64(true);
				String data = new String(decoder.decode(payload.getBytes()));
				
				//data = cleanJson(data);
				
				System.out.println("data from elements" + data);
				req.setAttribute("oauth", getOAuthToken(data));
				String accessToken = getOAuthToken(data);
				System.out.println("accessToken: " + accessToken);
				if(accessToken == null || "".equals(accessToken)) {
					req.setAttribute("sendRedirect", true);
				} else {
					req.setAttribute("sendRedirect", false);					
					req.setAttribute("checkins", getCheckInInfo(req, getOAuthToken(data)));
				}
			} else {
				req.setAttribute("sendRedirect", true);
			}
		} else {
			req.setAttribute("sendRedirect", true);
		}
		
		System.out.println("send redirect: " + req.getAttribute("sendRedirect"));
	    req.getRequestDispatcher("canvas.jsp").forward(req, resp);

	}
	
	//two ugly hacks. First of all the data decodes with control characters in it
	//the pattern will strip those. Then it either seems to cut off or come over incomplete
	//that is what the curly balance bit fixes. We're probably losing data, but since we only
	//care about the oauth token this seems to be OK
	private String cleanJson(String data) {
        String outputData = null;
		Pattern p = Pattern.compile("[\\x00-\\x1f]");
        Matcher m = p.matcher(data);
        outputData = m.replaceAll("");
		System.out.println("cleaned json: " + outputData);
		int curlyBalance = curlyBalance(outputData);
        for (int i=0; i<curlyBalance; i++) {
        	outputData = outputData + '}';
        }
        System.out.println("balanced json: " + outputData);
        return outputData;
	}
	
	//return a positive number if the number of { is greater
	//than the number of }
	private int curlyBalance(String data) {
		int count = 0;
		for(int i = 0; i< data.length(); i++) {
			if(data.charAt(i) == '{') {
				count++;
			}
			if(data.charAt(i) == '}') {
				count--;
			}
		}
		return count;
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
    
//    private String getCheckInInfo(HttpServletRequest req, String token) {
//        URL restUrl;
//        try {
//            restUrl = buildFBUrl(req, "/me/checkins", token);
//            return readApiData(restUrl);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    
    private String getCheckInInfo(HttpServletRequest req, String token) {
    	Facebook facebook = new FacebookTemplate(token);
    	List<Checkin> checkIns = facebook.placesOperations().getCheckins();
    	for (Checkin checkin : checkIns) {
			System.out.println("Check In: " + checkin.getPlace().getName());
		}
    	
    	URL restUrl;
    	try {
    		restUrl = buildFBUrl(req, "/me/checkins", token);
    		return readApiData(restUrl);
    	} catch (MalformedURLException e) {
    		e.printStackTrace();
    		return null;
    	}
    }

}
