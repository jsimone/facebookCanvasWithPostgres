package com.force.demo.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

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
				String payloadAfterReplace = payload.replaceAll("-_", "+/");
				System.out.println("payload after replace: " + payloadAfterReplace);
				String data = String.valueOf(DatatypeConverter.parseBase64Binary(payloadAfterReplace));
				System.out.println("data from elements" + data);
				req.setAttribute("test", data);
			} else {
				String payload = signed_request.substring(signed_request.indexOf('.'));
				String data = String.valueOf(DatatypeConverter.parseBase64Binary(payload.replaceAll("-_", "+/")));
				System.out.println("data from substr" + data);
				req.setAttribute("test", data);
			}
		}
		
		
	    req.getRequestDispatcher("facebook.jsp").forward(req, resp);

	}

}
