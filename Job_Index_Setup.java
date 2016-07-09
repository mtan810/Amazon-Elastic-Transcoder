import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoder;
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoderClient;
import com.amazonaws.services.elastictranscoder.model.CreateJobOutput;
import com.amazonaws.services.elastictranscoder.model.CreateJobRequest;
import com.amazonaws.services.elastictranscoder.model.JobInput;
import com.amazonaws.services.elastictranscoder.model.ListJobsByPipelineRequest;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

/**
 * This is part 2 of my final project demonstrating the usage of the Amazon Elastic Transcoder.
 * 
 * The purpose of this program is to set up the job and index/error files for website hosting. 
 * Here are the 3 main steps of this program:
 * 
 * 1. It creates an Amazon Elastic Transcoder job. It uses the pipeline ID that was displayed in the
 * first part of the project, Bucket_Pipeline_Setup.java. Once the job is finished, it will put it in
 * the output bucket.
 *  
 * 2. It creates index.html, which contains the transcoded video, and puts it in the output bucket.
 *  
 * 3. It creates error.html, which redirects to index.html, and puts it in the output bucket.
 */
public class Job_Index_Setup {

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

        // Name of output bucket
        String outputBucket = "etsoutputbucket";

        // Enter Pipeline ID of the pipeline that was created from Bucket_Pipeline_Setup
        String PIPELINE_ID = "1449955865518-cy405o";
        
        // Enter name of the video to be transcoded here
        String INPUT_KEY = "congrats";
        
        // Enter name of transcoded video here
        String OUTPUT_KEY = "congratsoutput";
        
        // Presets, found in Elastic Transcoder -> Presets
        // You can test each preset by replacing preset ID
        String PRESET_ID = "1351620000001-000001"; // 1080p
        // String PRESET_ID = "1351620000001-000010"; // 720p
        // String PRESET_ID = "1351620000001-000020"; // 480p 16:9
        // String PRESET_ID = "1351620000001-000030"; // 480p 4:3
        // String PRESET_ID = "1351620000001-000040"; // 360p 16:9
        // String PRESET_ID = "1351620000001-000050"; // 360p 4:3
        // String PRESET_ID = "1351620000001-000061"; // 320x240
        
        try {
            // Create job
            System.out.print("Creating Job... ");
            JobInput input = new JobInput()
                    .withKey(INPUT_KEY);
            CreateJobOutput output = new CreateJobOutput()
                    .withKey(OUTPUT_KEY)
                    .withPresetId(PRESET_ID);
            CreateJobRequest createJobRequest = new CreateJobRequest()
                    .withPipelineId(PIPELINE_ID)
                    .withInput(input)
                    .withOutputs(output);
            amazonElasticTranscoder.createJob(createJobRequest);
            System.out.println("DONE!");
            
            // List jobs
            System.out.println("Listing jobs... ");
            ListJobsByPipelineRequest listJobsByPipelineRequest = new ListJobsByPipelineRequest()
	    		  .withPipelineId(PIPELINE_ID);
            System.out.println(amazonElasticTranscoder.listJobsByPipeline(listJobsByPipelineRequest).toString());
            
            // Create index.html
            System.out.print("Creating index.html... ");
            File file = new File("index.html");
			Writer writer = new OutputStreamWriter(new FileOutputStream(file));
			writer.write("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1252\">\n");
			writer.write("<title>Elastic Transcoder Test</title>\n");
			writer.write("</head>\n");
			writer.write("<body>\n");
			writer.write("<h1>Elastic Transcoder Test</h1>\n");
			writer.write("<video width=\"1280\" height=\"720\" controls>\n");
			writer.write(" <source src=\"" + OUTPUT_KEY + "\" type=\"video/mp4\">\n");
			writer.write(" Your browser does not support HTML5 video.\n");
			writer.write("</video>\n");
			writer.write("</body></html>\n");
			writer.close();
			System.out.println("DONE!");
            
			// Put index.html in output bucket
            System.out.print("Putting index.html in " + outputBucket + "... ");
            s3.putObject(new PutObjectRequest(outputBucket, "index.html", new File("index.html")));
            System.out.println("DONE!");
            
            // Create error.html
            System.out.print("Creating error.html... ");
            file = new File("error.html");
            writer = new OutputStreamWriter(new FileOutputStream(file));
            writer.write("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1252\">\n");
			writer.write("<title>Error</title>\n");
			writer.write("</head>\n");
			writer.write("<body>\n");
			writer.write("<h1>Error!</h1>\n");
			writer.write("<p>Click <a href=\"https://s3.amazonaws.com/" + outputBucket + "/index.html\">here</a> to go back to the main page.</p>\n");
			writer.write("</body></html>\n");
			writer.close();
            System.out.println("DONE!");
            
            // Put error.html in output bucket
            System.out.print("Putting error.html in " + outputBucket + "... ");
            s3.putObject(new PutObjectRequest(outputBucket, "error.html", new File("error.html")));
            System.out.println("DONE!");

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
