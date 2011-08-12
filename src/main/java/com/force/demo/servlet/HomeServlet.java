package com.force.demo.servlet;

import java.io.IOException;
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
			System.out.println("elements " + elements);
			
			if(elements.length > 1) {			
				String payload = elements[1];
				System.out.println("payload: " + payload);
				//String payloadAfterReplace = payload.replaceAll("-_", "+/");
				//System.out.println("payload after replace: " + payloadAfterReplace);
				String data = new String(Base64.decodeBase64(payload.getBytes()));
				
		        Pattern p = Pattern.compile("{cntrl}");
		        Matcher m = p.matcher(data);
		        data = m.replaceAll("");
				
				System.out.println("data from elements" + data);
				req.setAttribute("sendRedirect", false);
				req.setAttribute("data", data);
				req.setAttribute("oauth", getOAuthToken(data));
			} else {
				req.setAttribute("sendRedirect", true);
			}
		}
		
	    req.getRequestDispatcher("facebook.jsp").forward(req, resp);

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

}
