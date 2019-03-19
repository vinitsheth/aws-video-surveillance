package com.example.demo;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="HelloServlet",urlPatterns="/helloservlet")
public class HelloServlet extends HttpServlet {
	
	@Override
	protected void doGet (HttpServletRequest req,
            HttpServletResponse resp)
            			throws ServletException, IOException {

			System.out.println("-- In MyServlet --");
			PrintWriter writer = resp.getWriter();
			writer.println("dummy response from MyServlet");
}
}
