package vinit.appServerMaven;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

/**
 * Hello world!
 *
 */
public class App 
{
	static AmazonSQS sqs;
	static AmazonS3 s3;
	static String bucketName="video-detection-results";
	static String requestQueueUrl = "https://sqs.us-west-1.amazonaws.com/835319047630/request.fifo";
	static String responseQueueUrl = "https://sqs.us-west-1.amazonaws.com/835319047630/response.fifo";
	
    public static void main( String[] args )
    {
    	AWSCredentials credentials = new BasicAWSCredentials(
				  "", 
				  ""
				);
    	
    	sqs = AmazonSQSClientBuilder.standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.US_WEST_1)
				  .build();
    	s3 = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_WEST_1)
				  .build();
    	
        System.out.println( "Hello World!" );
    }
}
