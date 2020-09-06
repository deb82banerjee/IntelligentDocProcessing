package com.cts.idp.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.cts.idp.controller.IdpController;
import com.cts.idp.dao.CustomerDetailsDao;
import com.cts.idp.dao.DummyResponse;
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
			userId = "SR"+userId;
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			Date date = new Date();
			String strDate= formatter.format(date);
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			Files dbFile = new Files(userId, fileName,file.getContentType(),"",false,false,false,false,file.getBytes(), strDate);
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
		Files file = fileRepo.findByFileNameUserId(userId, fileName);
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
		userId="SR"+userId;
		List<Files> files = fileRepo.findByUserId(userId);
		Map<String, CustomerDetailsDao> custMap = new HashMap<>();
		List<FileDetailsDao> fileDetails = new ArrayList<FileDetailsDao>();
		DummyResponse dummyDetails = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

		for(Files file : files) {
			/*
			 * try { FileSystemResource fileResource =
			 * getFileSystemResource(file.getFileName(), file.getData()); body.add("file",
			 * fileResource); HttpEntity<MultiValueMap<String, Object>> requestEntity = new
			 * HttpEntity<>(body, headers); dummyDetails = restTemplate.postForObject
			 * ("/file-upload",requestEntity, DummyResponse.class); //TODO: call
			 * Shuvrangshu's service for processing
			 * System.out.println("Result ****---->"+dummyDetails.getMessage());
			 * fileResource.getFile().delete(); }catch(Exception e) { e.printStackTrace(); }
			 */
			
			FileDetailsDao details = getDummyResponse(); //TODO: call Shuvrangshu's service for processing
			details.setFileName(file.getFileName());
			
			fileDetails.add(details);
			updateFileData(details,userId);
			custMap.put(file.getFileName(), details.getCustomerDetails());
			
		}
		Customer customer =  processClassfiedData(custMap, userId);
		return customer; 
	}
	private FileDetailsDao getDummyResponse() {
		// TODO Auto-generated method stub
		CustomerDetailsDao custDetails = new CustomerDetailsDao("","Sachin","Tendulkar","Ramesh","01/01/1990","Kolkata, West Bengal, 700091");
		FileDetailsDao fileDetails = new FileDetailsDao("abc.jpg","Pan",true,true,false,custDetails);
		return fileDetails;
		
	}
	private FileSystemResource getFileSystemResource(String fileName, byte[] data) throws IOException {
        File convFile = new File(fileName);
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(data);
        fos.close();
        return new FileSystemResource(convFile);
    }
	
	private void updateFileData(FileDetailsDao details, String userId) {
		// TODO Auto-generated method stub
		String customerName = details.getCustomerDetails().getFirstName()+" "+ details.getCustomerDetails().getMiddleName()+" "+details.getCustomerDetails().getLastName();
		fileRepo.updateByUserIdFileName(details.isInformationExtacted(), false, true, details.isDocumentClassified(), customerName, userId, details.getFileName());
		
	}
	private Customer processClassfiedData(Map<String, CustomerDetailsDao> custMap, String userId) {
		Customer customer = new Customer();
		custMap.entrySet().stream().forEach(map -> {
			if(!StringUtils.isEmpty(map.getValue().getFirstName())) {
				if(!StringUtils.isEmpty(customer.getFirstName())){
					if(!customer.getFirstName().equalsIgnoreCase(map.getValue().getFirstName())) {
						customer.setMessage("Mismatch in Customer First Name in the documents!!");
						fileRepo.updateProcessedToFalse(false,userId,map.getKey());//Updating file status to be non-processed
					}
				}else {
					customer.setFirstName(map.getValue().getFirstName());
					customer.setLastName(map.getValue().getLastName());
					customer.setMiddleName(map.getValue().getMiddleName());
					customer.setAddress(map.getValue().getAddress());
					customer.setUserId(userId);
					customer.setDob(map.getValue().getDob());
					fileRepo.updateProcessedToFalse(true,userId,map.getKey());//Updating file status to be processed
				}
			}
		});
		
		if(StringUtils.isEmpty(customer.getMessage())) {
			custRepo.save(customer);
		}
		return customer;
		
	}
	public List<Files> getDashboard() {
		// TODO Auto-generated method stub
		List<Files> fileList = fileRepo.findAll();
		List<Files> newFileList = fileList.stream().map(file ->{
			String url = MvcUriComponentsBuilder
			          .fromMethodName(IdpController.class, "getFile", file.getFileName(),file.getUserId()).build().toString();
			file.setUrl(url);
			return file;
		}).collect(Collectors.toList());
		
		return newFileList;
	}
	public boolean validateDetails(String userId) {
		// TODO Auto-generated method stub
		userId = "SR"+userId;
		try {
			fileRepo.validateCustomerDetails(true, userId);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	
}
