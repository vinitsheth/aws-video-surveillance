package com.videoSurveillance;

import java.util.List;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

public class LoadBalancer implements Runnable{
	static AmazonSQS sqs;
	static String requestQueueUrl;
	static String responseQueueUrl;
	
	public LoadBalancer(AmazonSQS s, String requrl ,String respurl ) {
		this.sqs = s;
		this.requestQueueUrl = requrl;
		this.responseQueueUrl = respurl;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Load Balancer running");
		while(true) {
			
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(requestQueueUrl);
			List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
//			List<Message> messages = sqs.receiveMessage(requestQueueUrl).getMessages();
			//System.out.println("Load Balancer running");
			for (Message m : messages) {
				String msg = m.getBody();
				int id = Integer.parseInt(msg);
				System.out.println("id recieved "+id);
				SendMessageRequest sendMessageRequest = new SendMessageRequest(responseQueueUrl,Integer.toString(id)+"@"+"DummyOutput"+Integer.toString(id));
				sendMessageRequest.setMessageGroupId("messageGroup1");
				
				SendMessageResult sendMessageResult = sqs.sendMessage(sendMessageRequest);
				String sequenceNumber = sendMessageResult.getSequenceNumber();
				String messageId = sendMessageResult.getMessageId();
				System.out.println("SendMessage succeed with messageId " + messageId + ", sequence number " + sequenceNumber 
				        + "\n");
				
				
//				SendMessageRequest send_msg_request = new SendMessageRequest()
//				        .withQueueUrl(requestQueueUrl)
//				        .withMessageBody(Integer.toString(id)+"$"+"DummyOutput");
//				sqs.sendMessage(send_msg_request);
//				System.out.println("Message Added in resp queue for "+id);
			    sqs.deleteMessage(requestQueueUrl, m.getReceiptHandle());
			}
		
		}
	}
}
