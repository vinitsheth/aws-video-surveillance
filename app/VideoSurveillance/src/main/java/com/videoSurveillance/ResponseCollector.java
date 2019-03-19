package com.videoSurveillance;

import java.util.*;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
public class ResponseCollector implements Runnable{
	
	
	static Map<Integer,String> match;
	static AmazonSQS sqs;
	static String responseurl;

	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Response Collector Running");
		while(true) {
			//System.out.println("Response Collector Running");
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(responseurl);
			List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
			for (Message m : messages) {
				String msgs [] = m.getBody().split("@",2);
				int id = Integer.parseInt(msgs[0]);
				System.out.println("id recieved "+id+" msg: "+msgs[1]);
				match.put(id, msgs[1]);
			    sqs.deleteMessage(responseurl, m.getReceiptHandle());
			}
		}
		
	}
	
	
	
	public ResponseCollector(Map<Integer,String> m , AmazonSQS amazonqueue, String url) {
		this.match = m;
		this.sqs = amazonqueue;
		this.responseurl = url;
	}
}
