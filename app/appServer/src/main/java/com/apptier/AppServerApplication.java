package com.apptier;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

@SpringBootApplication
@RestController
@EnableAutoConfiguration
@ServletComponentScan
public class AppServerApplication {

	
	static AmazonS3 s3;
	static String bucketName="video-detection-results";
	static AmazonSQS sqs;
	static String requestQueueUrl = "https://sqs.us-west-1.amazonaws.com/835319047630/request.fifo";
	static String responseQueueUrl = "https://sqs.us-west-1.amazonaws.com/835319047630/response.fifo";
	
	@RequestMapping("/RunScript")
	String runScript() {
		String s;
		String ans = "error oocured at app instance";
		try{
			 
			// run the Unix "ps -ef" command
            // using the Runtime exec method:
            Process p = Runtime.getRuntime().exec("python /home/ubuntu/darknet/generate_result.py");
            
            BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
            	ans=s;
                System.out.println(s);
            }
            
            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
            	//ans+=s;
                System.out.println(s);
            }
            
           
			
			
//			System.out.println("RunScript called"); 
//			ProcessBuilder pb = new ProcessBuilder("python","/home/ubuntu/darknet/generate_result.py");
//			Process p = pb.start();
//			System.out.println("Process started"); 
//			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			int i = p.exitValue();
//			ans = in.readLine();
//			
			
			String msgs [] = ans.split("@",2);
			
			s3.putObject(bucketName,msgs[0], msgs[1]);
//			
//			System.out.println(in.readLine());
			}catch(Exception e){
				ans = "Exception occured at app tier";
				System.out.println("Exception occured at app tier");
				
				e.printStackTrace();;}
			
		
		
		
		return ans;
	}
	
	
	
	@RequestMapping("/testS3")
	String testS3() {
		
		String ans = "error oocured at app instance";
		try{
			 
			ans = "DummyVideo@DummyLabel";
			String msgs [] = ans.split("@");
			System.out.println(msgs[0]);
			System.out.println(msgs[1]);
			s3.putObject(bucketName,msgs[0], msgs[1]);
			
			
			}catch(Exception e){
				ans = "Exception occured at app tier";
				System.out.println("Exception occured at app tier");
				
				System.out.println(e);}
			
		
		
		
		return ans;
	}
	
	
	@RequestMapping("/test")
	String runtest() {
		return "test successful final";
	}
	
	public static void main(String[] args) {
		System.out.println("#################VINIT######################");
		
		AWSCredentials credentials = new BasicAWSCredentials(
				  "", 
				  ""
				);
		
		s3 = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_WEST_1)
				  .build();
		sqs = AmazonSQSClientBuilder.standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.US_WEST_1)
				  .build();
		
		SpringApplication.run(AppServerApplication.class, args);
	}

}
