package com.example.demo.controller;

import java.util.Locale;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.application.service.UserApplicationService;
import com.example.demo.domain.user.model.MUser;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.form.GroupOrder;
import com.example.demo.form.SignupForm;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user")
@Slf4j
public class SignupController {

	@Autowired
	private UserApplicationService userApplicationService;

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	/**ユーザ登録画面を表示*/
	@GetMapping("/signup")
	public String getSignup(Model model, Locale locale, @ModelAttribute SignupForm form) {
		//性別を取得
		Map<String, Integer> genderMap = userApplicationService.getGendermap(locale);
		model.addAttribute("genderMap", genderMap);

		//ユーザ登録画面に遷移
		return "user/signup";
	}

	/**ユーザ登録処理*/
	@PostMapping("/signup")

	public String postSignup(Model model, Locale locale, @ModelAttribute @Validated(GroupOrder.class) SignupForm form,
			BindingResult bindingResult) {

		//入力チェック結果
		if (bindingResult.hasErrors()) {
			//NG:ユーザ登録画面に戻ります
			return getSignup(model, locale, form);
		}
		log.info(form.toString());

		//formをMUserクラスに変換
		MUser user = modelMapper.map(form, MUser.class);

		//ユーザ登録
		userService.signup(user);

		//ログイン画面にリダイレクト
		return "redirect:/login";
	}
}
