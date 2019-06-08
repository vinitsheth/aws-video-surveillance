package com.videoSurveillance;

import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;

@WebServlet(
		  name = "DetectObjectServlet",
		  description = "Servlet which listens to detect objects",
		  urlPatterns = {"/detectObjectsabcd"}
		)
public class DetectObjects extends HttpServlet{
	static Map<Integer,String> match;
	static int currentID = 0;
	static AmazonSQS sqs;
	static String requestQueueUrl = "https://sqs.us-west-1.amazonaws.com/835319047630/receive.fifo";
	static LoadBalancer myLoadBalancer;
	static ResponseCollector myResponseCollector;
	
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		 match = new HashMap<Integer,String>();
		
		 AWSCredentials credentials = new BasicAWSCredentials(
				  "", 
				  ""
				);
			
		sqs = AmazonSQSClientBuilder.standard()
					  .withCredentials(new AWSStaticCredentialsProvider(credentials))
					  .withRegion(Regions.US_WEST_1)
					  .build();
		myResponseCollector = new ResponseCollector(match, sqs);
		myLoadBalancer = new LoadBalancer(sqs);
		super.init();
	}
	
	@Override
    protected void doGet(
      HttpServletRequest request, 
      HttpServletResponse response) throws ServletException, IOException {
		int myid = currentID;
		currentID ++;
		match.compute(myid,null);
		
		SendMessageRequest send_msg_request = new SendMessageRequest()
		        .withQueueUrl(requestQueueUrl)
		        .withMessageBody(Integer.toString(myid));
		
		sqs.sendMessage(send_msg_request);
		System.out.println("Id Generated "+myid);
		while(match.get(myid)==null);
		String ans = match.get(myid);
		match.remove(myid);
		response.setContentType("text/plain;charset=UTF-8");

        ServletOutputStream sout = response.getOutputStream();
        sout.print(ans);
		
		
    }
	
	
	
	
	
}
