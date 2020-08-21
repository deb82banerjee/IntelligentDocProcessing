package com.cts.idp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.cts.idp.controller.IdpController;
import com.cts.idp.model.FileInfo;
import com.cts.idp.model.Files;
import com.cts.idp.repo.FileRepository;

@Service
public class FileService {
	
	@Autowired
	FileRepository fileRepo;

	public boolean storeFiles(MultipartFile file) {
		boolean isSuccess = false;
		try {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			Files dbFile = new Files("", fileName,file.getContentType(),file.getBytes());
			fileRepo.save(dbFile);
			isSuccess = true;
		}catch(Exception e) {
			isSuccess = false;
			e.printStackTrace();
		}
		return isSuccess;
	}
	public List<FileInfo> getFiles() {
		List<Files> files = fileRepo.findAll();
		List<FileInfo> fileInfo = files.stream().map(path -> {
		      String filename = path.getFileName().toString();
		      String url = MvcUriComponentsBuilder
	          .fromMethodName(IdpController.class, "getFile", path.getFileName().toString()).build().toString();
		      String deleteUrl = MvcUriComponentsBuilder
			          .fromMethodName(IdpController.class, "deleteFile", path.getFileName().toString()).build().toString();
		      return new FileInfo(filename, url, deleteUrl);
		    }).collect(Collectors.toList());
		return fileInfo;
	}
	
	public Files loadFile(String fileName) {
		Files file = fileRepo.findByFileName(fileName);
		
		return file;
	}
	public boolean deleteFile(String fileName) {
		try {
			fileRepo.deleteByFileName(fileName);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	public boolean processFiles() {
		//Need to send to Processing Engine
		List<Files> files = fileRepo.findAll();
		for(Files file : files) {
			byte[] fileData = file.getData();
			//TODO: call Shuvrangshu's service for processing
		}
		return true; // also this should return object
	}
	
	
}
