package com.videoSurveillance;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ObjectDetectorSSH implements Runnable {

	static String ip;
	static String keyValuePath;
	
	
	@Override
	public void run() {
		try {
		
		Process p = Runtime.getRuntime().exec("ssh -o \"StrictHostKeyChecking no\" ubuntu@13.56.233.178");
		PrintStream out = new PrintStream(p.getOutputStream());
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		out.println("Cloud12345!");
		out.println("ls -l /home/me");
		while (in.ready()) {
		  String s = in.readLine();
		  System.out.println(s);
		}
		out.println("exit");

		p.waitFor();
		// TODO Auto-generated method stub
		}catch(Exception e){
			System.out.println("ssh exception");
		}
		
	}
	
	public ObjectDetectorSSH() {
		
	}
	
	
}
