package com.videoSurveillance;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class HelloWorldResponseServlet extends HttpServlet{

	@Override
    protected void doGet(
      HttpServletRequest request, 
      HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<p>Hello World!</p>");
        
    }
	
	public void respond() {
		try {
        	FileInputStream fi = new FileInputStream(new File("myObjects.txt"));
			ObjectInputStream oi = new ObjectInputStream(fi);
			
			clientPOJO client = (clientPOJO)oi.readObject();
			doGet(client.request, client.response);
        }catch(Exception e) {
        	
        }
		
	}
	
	
	
}
