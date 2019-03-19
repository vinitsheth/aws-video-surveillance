package com.videoSurveillance;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;

import java.util.List;


public class SWSQueues {
	
	public void list() {
	
		AWSCredentials credentials = new BasicAWSCredentials(
			  "AKIAJN7UZDZO7RFC7FFA", 
			  "bHJ2NMJZjDA3+IyTUsbOE68xDkxJnVcypk9GVPOV"
			);
		
		AmazonSQS sqs = AmazonSQSClientBuilder.standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.US_WEST_1)
				  .build();
//		final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
		System.out.println("Listing all queues in your account.\n");
		for (final String queueUrl : sqs.listQueues().getQueueUrls()) {
		    System.out.println("  QueueUrl: " + queueUrl);
		}
		
		System.out.println();
		System.out.println("listed");
		
	}
	
}
