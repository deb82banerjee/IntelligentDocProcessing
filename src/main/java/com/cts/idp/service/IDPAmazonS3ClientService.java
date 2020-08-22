package com.cts.idp.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class IDPAmazonS3ClientService {
//	private AmazonS3 s3client;

    @Value("${aws.endpointUrl}")
    private String endpointUrl;
    @Value("${aws.bucketName}")
    private String bucketName;
    @Value("${aws.accessKey}")
    private String accessKey;
    @Value("${aws.secretKey}")
    private String secretKey;
    
    @PostConstruct
    private void initializeAmazon() {
		/*
		 * AWSCredentials credentials = new BasicAWSCredentials(this.accessKey,
		 * this.secretKey); this.s3client = new AmazonS3Client(credentials);
		 */
    }
    
    
    public String uploadFile(MultipartFile multipartFile) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
           e.printStackTrace();
        }
        return fileUrl;
    }

    public String deleteFileFromS3Bucket(String fileUrl) {
		/*
		 * String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		 * s3client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));
		 * return "Successfully deleted";
		 */
    	return null;
    }
    
    private void uploadFileTos3bucket(String fileName, File file) {
		/*
		 * s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
		 * .withCannedAcl(CannedAccessControlList.PublicRead));
		 */
    }
    
    
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
    
    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }
}
