package com.cts.idp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cts.idp.service.IDPAmazonS3ClientService;

@RestController
@RequestMapping("/idp/storage")
public class IDPAmazonS3RestController {

	@Autowired
	private IDPAmazonS3ClientService amazonS3Service;

    @Autowired
    IDPAmazonS3RestController(IDPAmazonS3ClientService amazonS3Service) {
        this.amazonS3Service = amazonS3Service;
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return this.amazonS3Service.uploadFile(file);
    }

    @DeleteMapping("/deleteFile")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        return this.amazonS3Service.deleteFileFromS3Bucket(fileUrl);
    }
}
