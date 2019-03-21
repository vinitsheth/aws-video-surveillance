package com.videoSurveillance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

public class LoadBalancer implements Runnable{
	static AmazonSQS sqs;
	static String requestQueueUrl;
	static String responseQueueUrl;
	static AmazonEC2 ec2;
	static List<Instance> idle;
	static List<Instance> stopped;
	static List<Instance> running;
	static Map<Instance,Integer> instanceLoad;
	
	
	public LoadBalancer(AmazonSQS s, AmazonEC2 e, String requrl ,String respurl ) {
		sqs = s;
		ec2 =e;
		requestQueueUrl = requrl;
		responseQueueUrl = respurl;
		idle = new ArrayList<Instance>();
		stopped = new ArrayList<Instance>();
		running = new ArrayList<Instance>();
		instanceLoad = new HashMap<Instance, Integer>();
		generateLists();
			
	}
	
	private static void generateLists() {
		
		
		
		DescribeInstancesRequest request = new DescribeInstancesRequest();
		boolean done = false;
		while(!done) {
		DescribeInstancesResult response = ec2.describeInstances(request);
		
		
		for(Reservation reservation : response.getReservations()) {
	        for(Instance instance : reservation.getInstances()) {
	        	//boolean isapp = false;
	        
	        	 if (instance.getTags() != null) {
	                 for (Tag tag : instance.getTags()) {
	                	
	                	 if (tag.getKey().equals("Nature") && tag.getValue().equals("App")) {
	                		 
	                		 if (instance.getState().getName().equals("running")) {
	                			 running.add(instance);
	                			 System.out.println(instance.getInstanceId()+" added to running");
	                		 }else if(instance.getState().getName().equals("stopped")) {
	                			 stopped.add(instance);
	                			 System.out.println(instance.getInstanceId()+" added to stopped");
	                		 }
	                	 }
	      
	                 }
	             }
	        	 
	        	 //System.out.println(isapp);
	            	
	        }
	    }
		
		request.setNextToken(response.getNextToken());

	    if(response.getNextToken() == null) {
	        done = true;
	    }
	}
		
		
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Load Balancer running");
			
		
//		boolean done = false;
//
//		DescribeInstancesRequest request = new DescribeInstancesRequest();
//		while(!done) {
//		    DescribeInstancesResult response = ec2.describeInstances(request);
//
//		    for(Reservation reservation : response.getReservations()) {
//		        for(Instance instance : reservation.getInstances()) {
//		        	if (instance.getTags() != null) {
//	                 for (Tag tag : instance.getTags()) {
//	                	
//	                	 if (tag.getKey().equals("Nature") && tag.getValue().equals("App")) {
//	                		 
//	                		 if (instance.getState().getName().equals("running")) {
//	                			 running.add(instance.getInstanceId());
//	                			 System.out.println(instance.getInstanceId()+" added to running");
//	                		 }else if(instance.getState().getName().equals("stopped")) {
//	                			 stopped.add(instance.getInstanceId());
//	                			 System.out.println(instance.getInstanceId()+" added to stopped");
//	                		 }
//	                	 }
//	      
//	                 }
//	             }
//		        }
//		    }
//
//		    request.setNextToken(response.getNextToken());
//
//		    if(response.getNextToken() == null) {
//		        done = true;
//		    }
//		}
		int tempint = 1;
		while(tempint!=0) {
			
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(requestQueueUrl);
			List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
//			List<Message> messages = sqs.receiveMessage(requestQueueUrl).getMessages();
			//System.out.println("Load Balancer running");
			
			for (Message m : messages) {
				tempint --;
				String msg = m.getBody();
				int id = Integer.parseInt(msg);
				
				
				System.out.println("id recieved "+id);
				
				Instance temp = stopped.remove(0);
				Thread tempRunner = new Thread(new InstanceRunner(sqs, ec2, responseQueueUrl, idle, stopped, running, instanceLoad, temp, id));
				tempRunner.start();
				
//				SendMessageRequest sendMessageRequest = new SendMessageRequest(responseQueueUrl,Integer.toString(id)+"@"+"DummyOutput"+Integer.toString(id));
//				sendMessageRequest.setMessageGroupId("messageGroup1");
//				
//				
//				
//				SendMessageResult sendMessageResult = sqs.sendMessage(sendMessageRequest);
//				String sequenceNumber = sendMessageResult.getSequenceNumber();
//				String messageId = sendMessageResult.getMessageId();
//				System.out.println("SendMessage succeed with messageId " + messageId + ", sequence number " + sequenceNumber 
//				        + "\n");
//				
//				
////				SendMessageRequest send_msg_request = new SendMessageRequest()
////				        .withQueueUrl(requestQueueUrl)
////				        .withMessageBody(Integer.toString(id)+"$"+"DummyOutput");
////				sqs.sendMessage(send_msg_request);
////				System.out.println("Message Added in resp queue for "+id);
			    //sqs.deleteMessage(requestQueueUrl, m.getReceiptHandle());
			}
		
		}
	}
}
