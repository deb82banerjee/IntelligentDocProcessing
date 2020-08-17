package com.cts.idp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/idp")
public class IdpController {

	@GetMapping("/welcome")
	public String getWelcomeScreen() {
		return "welcome";
	}
	@GetMapping("/start")
	public String getStartScreen() {
		return "start";
	}
}
