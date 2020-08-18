package com.cts.idp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/idp")
public class IdpController {

	@Autowired
	RestTemplate restTemplate;
	
	@GetMapping("/welcome")
	public String getWelcomeScreen() {
		return "welcome";
	}
	@GetMapping("/start")
	public String getStartScreen() {
		return "start";
	}
	
	@PostMapping("/startUpload")
	public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
        String response = restTemplate.postForObject("/uploadFile", file, String.class);
        return response;
	}
}
