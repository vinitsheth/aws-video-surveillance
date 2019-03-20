

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ObjectDetectorSSH implements Runnable {

	static String ip;
	static String keyValuePath;
	
	
	@Override
	public void run() {
		System.out.println("Running");
		
		
		try{
			 
			 
			ProcessBuilder pb = new ProcessBuilder("python","/home/vinit/Desktop/Github/aws-video-surveillance/scripts/test.py");
			Process p = pb.start();
			 
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			System.out.println(in.readLine());
			}catch(Exception e){System.out.println(e);}
			}
		
	
	
	public ObjectDetectorSSH() {
		
	}
	public static void main(String[] args) {
		Thread obj = new Thread(new ObjectDetectorSSH());
		obj.start();
	}
	
}
