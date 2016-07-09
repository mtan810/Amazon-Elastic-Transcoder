import java.io.File;
import java.io.IOException;

import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoder;
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoderClient;
import com.amazonaws.services.elastictranscoder.model.CreatePipelineRequest;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectRequest;

/**
 * This is part 1 of my final project demonstrating the usage of the Amazon Elastic Transcoder.
 * 
 * The purpose of this program is to set up the buckets and pipeline. Here are the 3 main steps
 * of this program:
 * 
 * 1. It creates two Amazon S3 buckets, an input bucket for the video to transcode,
 * and an output bucket for the transcoded video. The output bucket will also hold 2 HTML files,
 * "index.html" and "error.html", for website hosting.
 *  
 * 2. It uploads a video to transcode into the input bucket. The video should be placed within
 * the project directory. You will need to enter the name and file type of the video within the code.
 *  
 * 3. It creates an Amazon Elastic Transcoder pipeline and displays its ID in the console terminal output. 
 * This ID will be used for part 2 of my final project, Job_Index_Setup.java.
 */
public class Bucket_Pipeline_Setup {

    public static void main(String[] args) throws IOException, InterruptedException {

        /*
         * The ProfileCredentialsProvider will return your
         * credential profile by reading from the credentials file.
         */
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("MasonJavaSDK").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (C:\\Users\\user\\.aws\\credentials), and is in valid format.",
                    e);
        }

        AmazonS3 s3 = new AmazonS3Client(credentials);
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        s3.setRegion(usEast1);
        AmazonElasticTranscoder amazonElasticTranscoder = new AmazonElasticTranscoderClient();
        
        // Name of input bucket
        String inputBucket = "etsinputbucket";
        
        // Name of output bucket
        String outputBucket = "etsoutputbucket";
        
        // Name of pipeline
        String pipelineName = "myPipeline";
        
        // IAM role for pipeline, this is my default IAM role
        // This could be found in IAM service
        String role = "arn:aws:iam::600384891926:role/Elastic_Transcoder_Default_Role";
        
        // Enter name and file type of the video to be transcoded here
        String INPUT_KEY = "congrats";
        String INPUT_KEY_TYPE = ".webm";
        
        try {
        	// Create input bucket
            System.out.print("Creating input bucket " + inputBucket + "... ");
            Boolean flag = true;
            for (Bucket bucket : s3.listBuckets()) {
                if (bucket.getName().equals(inputBucket)) {
                	flag = false;
                }
            }
            if (flag) {
            	s3.createBucket(inputBucket);
            	System.out.println("DONE!");
            }
            else {
            	System.out.println("Input bucket already exists!");
            }
            
            // Create output bucket
            System.out.print("Creating output bucket " + outputBucket + "... ");
            flag = true;
            for (Bucket bucket : s3.listBuckets()) {
                if (bucket.getName().equals(outputBucket)) {
                	flag = false;
                }
            }
            if (flag) {
            	s3.createBucket(outputBucket);
            	System.out.println("DONE!");
            }
            else {
            	System.out.println("Output bucket already exists!");
            }

            // List buckets
            System.out.println("Listing buckets");
            for (Bucket bucket : s3.listBuckets()) {
                System.out.println(" - " + bucket.getName());
            }
            System.out.println();
            
			// Upload video in input bucket
			System.out.print("Putting video " + "\"" + INPUT_KEY + "\" in " + inputBucket + "... ");
			s3.putObject(new PutObjectRequest(inputBucket, INPUT_KEY, new File(INPUT_KEY + INPUT_KEY_TYPE)));
			System.out.println("DONE!");
            
            // Create pipeline
			System.out.print("Creating pipeline " + pipelineName + "... ");
            CreatePipelineRequest createPipelineRequest = new CreatePipelineRequest()
            		.withInputBucket(inputBucket)
            		.withOutputBucket(outputBucket)
            		.withName(pipelineName)
            		.withRole(role);
            amazonElasticTranscoder.createPipeline(createPipelineRequest);
            System.out.println("DONE!");

            // List pipelines
            System.out.println("Listing pipelines");
            System.out.println(amazonElasticTranscoder.listPipelines().toString());
            
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
}
