package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class LogoutController {
	
	/**ログイン画面にリダイレクト*/
	@PostMapping("/logput")
	public String postLogput() {
		log.info("ログアウト");
		return "redirect:/login";
	}
}