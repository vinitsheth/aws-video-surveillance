package com.videoSurveillance;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
		  name = "helloworldexample",
		  description = "Example Servlet Using Annotations",
		  urlPatterns = {"/helloworld"}
		)
public class HelloWorldServlet extends HttpServlet{

	@Override
    protected void doGet(
      HttpServletRequest request, 
      HttpServletResponse response) throws ServletException, IOException {
		
		new SWSQueues().list();
		
		
        
    }
	
	
	
}
