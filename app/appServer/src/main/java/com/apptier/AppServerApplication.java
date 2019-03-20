package com.apptier;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableAutoConfiguration
@ServletComponentScan
public class AppServerApplication {

	
	@RequestMapping("/RunScript")
	String runScript() {
		
		String ans = "error oocured at app instance";
		try{
			 
			System.out.println("RunScript called"); 
			ProcessBuilder pb = new ProcessBuilder("python","/home/ubuntu/darknet/generate_result.py");
			Process p = pb.start();
			System.out.println("Process started"); 
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			ans = in.readLine();
			System.out.println(in.readLine());
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
		SpringApplication.run(AppServerApplication.class, args);
	}

}
