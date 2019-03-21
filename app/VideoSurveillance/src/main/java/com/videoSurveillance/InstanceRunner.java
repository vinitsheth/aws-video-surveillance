package com.videoSurveillance;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.lightsail.model.StartInstanceRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

public class InstanceRunner implements Runnable{
	
	 AmazonSQS sqs;
	 AmazonEC2 ec2;
	 String responseQueueUrl;
	 List<Instance> idle;
	 List<Instance> stopped;
	 List<Instance> running;
	 Map<Instance,Integer> instanceLoad;
	 int id;
	 Instance instance;
	public InstanceRunner(AmazonSQS lsqs,AmazonEC2 lec2,String lresponseQueueUrl,List<Instance> lidle,
			List<Instance> lstopped,List<Instance> lrunning,Map<Instance,Integer> linstanceLoad,Instance linstance, int i) {
		sqs = lsqs;
		ec2 = lec2;
		responseQueueUrl = lresponseQueueUrl;
		idle = lidle;
		stopped = lstopped;
		running = lrunning;
		instanceLoad = linstanceLoad;
		instance = linstance;
		id = i;
		
		System.out.println("Instance Runner Called");
		
		//Start instance if stopped
		
		
		
		
		
	}
	
	
	 void startInstance() {
		StartInstancesRequest req = new StartInstancesRequest()
				.withInstanceIds(instance.getInstanceId());
		ec2.startInstances(req);
		ArrayList<String> tempinstanceids = new ArrayList<String>();
		tempinstanceids.add(instance.getInstanceId());
		
		while(!instance.getState().getName().equals("running")) {
			
			
			DescribeInstancesRequest request = new DescribeInstancesRequest();
			request.setInstanceIds(tempinstanceids);
			DescribeInstancesResult response = ec2.describeInstances(request);

			for(Reservation reservation : response.getReservations()) {
		        for(Instance i : reservation.getInstances()) {
		        		instance = i;
		        		System.out.println(i.getState().getName());
		        }
			}
		}
		
		
	}
	
	@Override
	public void run() {
		if(instance.getState().getName().equals("stopped")) {
			startInstance();
		}
		System.out.println("Instance Started");
		StringBuffer content= new StringBuffer("No output form instance Runner");
		String ip = instance.getPublicIpAddress();
		
		
		try {
			System.out.println(ip);
			URL url = new URL("http://"+ip+":8080/test");
			
			
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(0);
			con.setReadTimeout(0);
			int status = con.getResponseCode();
			BufferedReader in = new BufferedReader(
					  new InputStreamReader(con.getInputStream()));
					String inputLine;
					 content = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
					    content.append(inputLine);
					}
					
					//System.out.println(content);
					in.close();
					
			con.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		SendMessageRequest sendMessageRequest = new SendMessageRequest(responseQueueUrl,new String(content));
		sendMessageRequest.setMessageGroupId("messageGroup1");
		
		
		
		SendMessageResult sendMessageResult = sqs.sendMessage(sendMessageRequest);
		String sequenceNumber = sendMessageResult.getSequenceNumber();
		String messageId = sendMessageResult.getMessageId();
		System.out.println("SendMessage succeed with messageId " + messageId + ", sequence number " + sequenceNumber 
		        + "\n");
		
		
//		SendMessageRequest send_msg_request = new SendMessageRequest()
//		        .withQueueUrl(requestQueueUrl)
//		        .withMessageBody(Integer.toString(id)+"$"+"DummyOutput");
//		sqs.sendMessage(send_msg_request);
//		System.out.println("Message Added in resp queue for "+id);
	    
		
		
		// TODO Auto-generated method stub
		
		
		
	}
	
}
