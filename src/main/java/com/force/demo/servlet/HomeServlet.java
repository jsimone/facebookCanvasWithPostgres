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
		
		System.out.println("signed request: " + signed_request);
		String[] elements = signed_request.split(".");
		
		if(elements.length > 1) {			
			System.out.println("elements " + elements);
			String encodedSignature = elements[0];
			String payload = elements[1];
			String data = String.valueOf(DatatypeConverter.parseBase64Binary(payload.replaceAll("-_", "+/")));
			System.out.println("data " + data);
			req.setAttribute("test", data);
		}
		
		
	    req.getRequestDispatcher("facebook.jsp").forward(req, resp);

	}

}
