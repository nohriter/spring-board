package com.nori.springboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home() {
		return "redirect:/board/lists";
	}

	@ResponseBody
	@GetMapping("/healthcheck")
	public String healthcheck() {
		return "OK";
	}

}
