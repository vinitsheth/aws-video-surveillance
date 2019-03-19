package com.videoSurveillance;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;




@SpringBootApplication
@RestController
@EnableAutoConfiguration
@ServletComponentScan
public class VideoSurveillanceApplication {
	
	static Map<Integer,String> match;
	static int currentID = 0;
	static AmazonSQS sqs;
	static String requestQueueUrl = "https://sqs.us-west-1.amazonaws.com/835319047630/request.fifo";
	static String responseQueueUrl = "https://sqs.us-west-1.amazonaws.com/835319047630/response.fifo";
	static LoadBalancer myLoadBalancer;
	static ResponseCollector myResponseCollector;
	
	
	@RequestMapping("/test")
	String runtest() {
		return "test successful final";
	}
	
	
	@RequestMapping("/getObject")
	String objectGetter() {
		int myid = currentID;
		currentID ++;
		match.put(myid,null);
		
		SendMessageRequest sendMessageRequest = new SendMessageRequest(requestQueueUrl,Integer.toString(myid));
		sendMessageRequest.setMessageGroupId("messageGroup1");
		SendMessageResult sendMessageResult = sqs.sendMessage(sendMessageRequest);
		String sequenceNumber = sendMessageResult.getSequenceNumber();
		String messageId = sendMessageResult.getMessageId();
		System.out.println("SendMessage succeed with messageId " + messageId + ", sequence number " + sequenceNumber 
		        + "\n");
		
//		SendMessageRequest send_msg_request = new SendMessageRequest()
//		        .withQueueUrl(requestQueueUrl)
//		        .withMessageBody(Integer.toString(myid));
//		sqs.sendMessage(send_msg_request);
		System.out.println("Id Generated "+myid);
		while(match.get(myid)==null);
		String ans = match.get(myid);
		match.remove(myid);
		return ans;
	}
	
	
	public static void main(String[] args) {
		//set matcher hash table
		
		match = new HashMap<Integer,String>();
		System.out.println("Hash Map initited");
		
		// Pass AWS Credentials
		AWSCredentials credentials = new BasicAWSCredentials(
				  "AKIAJN7UZDZO7RFC7FFA", 
				  "bHJ2NMJZjDA3+IyTUsbOE68xDkxJnVcypk9GVPOV"
				);
		
		System.out.println("Credentails Passed");
		
		
		//Generate SQS Object
		sqs = AmazonSQSClientBuilder.standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.US_WEST_1)
				  .build();
		System.out.println("SQS object Instintated");
		
		//Set Response collector and LoadBalancer
		
		Thread responseCollctorThread = new Thread(new ResponseCollector(match, sqs, responseQueueUrl));
		responseCollctorThread.start();
		System.out.println("Response Collector Initialized");
		
		Thread loadBalancerThread = new Thread(new LoadBalancer(sqs,requestQueueUrl,responseQueueUrl));
		loadBalancerThread.start();
		
		System.out.println("LoadBalancer Collector Initialized");
		
		
		
		SpringApplication.run(VideoSurveillanceApplication.class, args);
	}

}
