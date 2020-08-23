package com.cts.idp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.cts.idp.message.ResponseMessage;
import com.cts.idp.model.Customer;
import com.cts.idp.model.FileInfo;
import com.cts.idp.model.Files;
import com.cts.idp.service.FileService;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/idp")
public class IdpController {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	FileService service;

	@GetMapping("/welcome")
	public String getWelcomeScreen() {
		return "welcome";
	}

	@GetMapping("/start")
	public String getStartScreen() {
		return "start";
	}

	@GetMapping("/files")
	public ResponseEntity<List<FileInfo>> getListFiles(@RequestParam("userId") String userId) {
		List<FileInfo> fileInfo = service.getFiles(userId);
		return ResponseEntity.status(HttpStatus.OK).body(fileInfo);
	}

	@PostMapping("/startUpload")
	public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
		String response = restTemplate.postForObject("/uploadFile", file, String.class);
		return response;
	}

	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadDocument(@RequestParam("file") MultipartFile file, @RequestParam("userId") String userId) {
		String message = "";
		boolean successful = service.storeFiles(file, userId);
		if (successful) {
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		}
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	}

	@GetMapping("/files/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename, @RequestParam("userId") String userId) {
		Files file = service.loadFile(filename, userId);

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
				.body(new ByteArrayResource(file.getData()));
	}

	@GetMapping("/delete/{filename:.+}")
	public ResponseEntity<ResponseMessage> deleteFile(@PathVariable String filename, @RequestParam("userId") String userId) {
		boolean success = service.deleteFile(filename, userId);
		String message = "";
		if (success) {
			message = "Deleted the file successfully: " + filename;
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		}
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	}
	@GetMapping("/processFiles")
	public ResponseEntity<Customer> processFiles(@RequestParam("userId") String userId) {
		Customer cust = service.processFiles(userId);
		return ResponseEntity.status(HttpStatus.OK).body(cust);
	}
	@GetMapping("/dashboard")
	public ResponseEntity<List<Files>> getAdminDashboard() {
		List<Files> files = service.getDashboard();
		return ResponseEntity.status(HttpStatus.OK).body(files);
	}
}
