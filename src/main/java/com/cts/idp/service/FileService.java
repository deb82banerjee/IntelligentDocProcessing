package com.cts.idp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.cts.idp.controller.IdpController;
import com.cts.idp.dao.CustomerDetailsDao;
import com.cts.idp.dao.FileDetailsDao;
import com.cts.idp.model.Customer;
import com.cts.idp.model.FileInfo;
import com.cts.idp.model.Files;
import com.cts.idp.repo.CustomerRepository;
import com.cts.idp.repo.FileRepository;

@Service
public class FileService {
	
	@Autowired
	FileRepository fileRepo;
	
	@Autowired
	CustomerRepository custRepo;
	
	@Autowired
	RestTemplate restTemplate;

	public boolean storeFiles(MultipartFile file, String userId) {
		boolean isSuccess = false;
		try {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			Files dbFile = new Files(userId, fileName,file.getContentType(),"",false,false,false,false,file.getBytes());
			fileRepo.save(dbFile);
			isSuccess = true;
		}catch(Exception e) {
			isSuccess = false;
			e.printStackTrace();
		}
		return isSuccess;
	}
	public List<FileInfo> getFiles(String userId) {
		List<Files> files = fileRepo.findByUserId(userId);
		List<FileInfo> fileInfo = files.stream().map(path -> {
		      String filename = path.getFileName().toString();
		      String url = MvcUriComponentsBuilder
	          .fromMethodName(IdpController.class, "getFile", path.getFileName(),path.getUserId()).build().toString();
		      String deleteUrl = MvcUriComponentsBuilder
			          .fromMethodName(IdpController.class, "deleteFile", path.getFileName(), path.getUserId()).build().toString();
		      return new FileInfo(filename, url, deleteUrl);
		    }).collect(Collectors.toList());
		return fileInfo;
	}
	
	public Files loadFile(String fileName, String userId) {
		Files file = fileRepo.findByFileNameUserId(fileName, userId);
		return file;
	}
	public boolean deleteFile(String fileName, String userId) {
		try {
			fileRepo.deleteByFileName(userId, fileName);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	public Customer processFiles(String userId) {
		//Need to send to Processing Engine
		List<Files> files = fileRepo.findByUserId(userId);
		Map<String, CustomerDetailsDao> custMap = new HashMap<>();
		List<FileDetailsDao> fileDetails = new ArrayList<FileDetailsDao>();
		for(Files file : files) {
			FileDetailsDao details = restTemplate.postForObject("/process", file, FileDetailsDao.class); //TODO: call Shuvrangshu's service for processing
			details.setFileName(file.getFileName());
			fileDetails.add(details);
			updateFileData(details,userId);
			custMap.put(file.getFileName(), details.getCustomerDetails());
		}
		Customer customer =  processClassfiedData(custMap, userId);
		return customer; 
	}
	private void updateFileData(FileDetailsDao details, String userId) {
		// TODO Auto-generated method stub
		fileRepo.updateByUserIdFileName(details.isInformationExtacted(), false, true, details.isDocumentClassified(), userId, details.getFileName());
		
	}
	private Customer processClassfiedData(Map<String, CustomerDetailsDao> custMap, String userId) {
		Customer customer = new Customer();
		custMap.entrySet().stream().forEach(map -> {
			if(!StringUtils.isEmpty(map.getValue().getFirstName())) {
				if(!StringUtils.isEmpty(customer.getFirstName())){
					if(!customer.getFirstName().equalsIgnoreCase(map.getValue().getFirstName())) {
						customer.setMessage("Mismatch in Customer First Name in the documents!!");
					}
				}else {
					customer.setFirstName(map.getValue().getFirstName());
					customer.setLastName(map.getValue().getLastName());
					customer.setMiddleName(map.getValue().getMiddleName());
					customer.setAddress(map.getValue().getAddress());
					customer.setUserId(userId);
					customer.setDob(map.getValue().getDob());
				}
			}
		});
		
		if(StringUtils.isEmpty(customer.getMessage())) {
			custRepo.save(customer);
		}
		return customer;
		
	}
	
	
}
