package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.elasticmapreduce.util.BootstrapActions.ConfigureDaemons;




@SpringBootApplication
@RestController
@EnableAutoConfiguration
@ServletComponentScan
public class DemoApplication extends SpringBootServletInitializer{
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DemoApplication.class);
	}
	
	
	@RequestMapping("/hello")
	String home() {
		EC2Example ec2 = new EC2Example();
		ec2.createinstance();
		
		return "Instance Created";
	}
	
	@RequestMapping("/test")
	String runtest() {
		return "test successful 102";
	}
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}


 class EC2Example {

    public static void createinstance() {

        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        System.out.println("create an instance");

        String imageId = "ami-0019ef04ac50be30f";  //image id of the instance
        int minInstanceCount = 1; //create 1 instance
        int maxInstanceCount = 1;

        RunInstancesRequest rir = new RunInstancesRequest(imageId,
                minInstanceCount, maxInstanceCount);
        rir.setInstanceType("t2.micro"); //set instance type

        RunInstancesResult result = ec2.runInstances(rir);

        List<Instance> resultInstance =
                result.getReservation().getInstances();

        for(Instance ins : resultInstance) {
            System.out.println("New instance has been created:" +
                    ins.getInstanceId());//print the instance ID
        }
    }

    public static void startinstance(String instanceId) {
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
        StartInstancesRequest request = new StartInstancesRequest().
                withInstanceIds(instanceId);//start instance using the instance id
        ec2.startInstances(request);

    }

    public static void stopinstance(String instanceId) {
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
        StopInstancesRequest request = new StopInstancesRequest().
                withInstanceIds(instanceId);//stop instance using the instance id
        ec2.stopInstances(request);

    }

    public static void terminateinstance(String instanceId) {
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
        TerminateInstancesRequest request = new TerminateInstancesRequest().
                withInstanceIds(instanceId);//terminate instance using the instance id
        ec2.terminateInstances(request);

    }
    /*
    public static void main(String[] args) {
        EC2Example ec2 = new EC2Example();
        //ec2.createinstance();
        //ec2.stopinstance("i-042291ade4afb1752");
        //ec2.startinstance("i-042291ade4afb1752");
        ec2.terminateinstance("i-042291ade4afb1752");
    }
	*/

}

