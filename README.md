# Amazon Elastic Transcoder

## Problem
Host a website which includes a video that has been transcoded via Amazon Elastic Transcoder. The website will be hosted using Amazon S3. The problem will serve as an instructive tutorial for beginners by examining the Amazon Elastic Transcoder in detail, using the AWS Management Console and AWS Java SDK.

## Description
Amazon Elastic Transcoder is a service used to transcode media files in the cloud. This is Amazon's solution for developers and businesses to effectively transcode media files from their source format into different versions that will playback on different devices such as smartphones, tablets and PCs. This project will detail the usage of this service by transcoding a video and hosting it on a website.

## Benefits
Pros: elastically scalable, cost effective, support for many common output formats
Cons: limit on number of pipelines, jobs, and presets, no comprehensive list of input formats

## Operating System
Windows 7 and up

## Software
Amazon Elastic Transcoder, Amazon S3, Java 8, Eclipse

## Overview of steps
1. Install and configure Eclipse and AWS SDK.
2. Create a new AWS Project and place 2 files in the source folder of the project directory: Bucket_Pipeline_Setup.java and Job_Index_Setup.java.
3. Get any video file to transcode and place it in the project directory. Enter the name and file type of the video in the appropriate parameters within Bucket_Pipeline_Setup.java.
4. Run Bucket_Pipeline_Setup.java.
5. Enter the pipeline ID from step 4 in the appropriate parameter within Job_Index_Setup.java and run Job_Index_Setup.java.
6. Grant permissions for the transcoded video, index.html, and error.html in S3 output bucket.
7. Enable website hosting and check website to see the transcoded video.

## Overview of files
* Bucket_Pipeline_Setup.java - Part 1 of my final project. For Eclipse, import this file in the src folder of a new AWS Project. Then run the program as a Java Application. This program will set up the buckets and pipeline for the project. Make sure this file is in the same src folder as Job_Index_Setup.java. Make sure to change parameters near the beginning of the code to suit your need. Make sure to include a video in the project directory so that this program can upload it to S3. I provided a sample video file “congrats".
* Job_Index_Setup.java - Part 2 of my final project. For Eclipse, import this file in the src folder of a new AWS Project. Then run the program as a Java Application. This program will set up the job and HTML files for the project. Make sure this file is in the same src folder as Bucket_Pipeline_Setup.java. Make sure to change parameters near the beginning of the code to suit your need. Make sure to run this program only after running Bucket_Pipeline_Setup and retrieving the pipeline ID from the display output.
* congrats - This is a small webm file that I used for demoing my final project. This video file should be in the same project directory as Job_Index_Setup.java and Bucket_Pipeline_Setup.java. Job_Index_Setup will upload this video into S3. You can use any video you want, but make sure to change the parameters within Job_Index_Setup for your video.
* congratsoutput - This is the transcoded video file for "congrats". In my demo, this is the transcoded video that has been created by the job in Job_Index_Setup. It is also the same video that you see in the website (http://etsoutputbucket.s3-website-us-east-1.amazonaws.com/).
* index.html - This is the index.html file created in Job_Index_Setup of my demo. This index.html file contains the video "congratsoutput". You can see the website here: http://etsoutputbucket.s3-website-us-east-1.amazonaws.com/.
* error.html - This is the error.html created in Job_Index_Setup of my demo. It will redirect to index.html if there is an error.

## Summary
Amazon Elastic Transcoder transcodes media files in the cloud. It is highly scalable, easy to use, and cost effective. There is good support for output formats but there is no comprehensive list of input formats. There is also a limit on the number of pipelines, jobs, and presets. Based on my experience, it is very programmatically accessible. The entire project is mostly done using the AWS SDK for Java. The documentation is very well written.

## References
http://docs.aws.amazon.com/elastictranscoder/latest/developerguide/introduction.html 
http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/elastictranscoder/AmazonElasticTranscoderClient.html 

## Links to YouTube videos
Short Video: https://www.youtube.com/watch?v=zhseLHXw-Js 
Long Video: https://www.youtube.com/watch?v=nex_VgGgAnY 
