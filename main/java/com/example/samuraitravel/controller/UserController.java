package com.example.samuraitravel.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.UserEditForm;
import com.example.samuraitravel.repository.UserRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	//	会員情報の閲覧、編集を担当するコントローラー

	private final UserRepository userRepository;
	private final UserService userService;

	public UserController(UserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;

	}

	@GetMapping
	//	ログイン中のユーザーの情報を取得するメソッド
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
		//		@AuthenticationPrincipanを引数に付与すると、現在ログイン中のユーザー情報を取得できる

		model.addAttribute("user", user);

		return "user/index";
		//		おそらくユーザーの会員トップページ

	}

	@GetMapping("/edit")
	public String edit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model ) {
		User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
		
		UserEditForm userEditForm = new UserEditForm(user.getId(),user.getName(),user.getFurigana(),
				user.getPostalCode(),user.getAddress(),user.getPhoneNumber(),user.getEmail());
		
		model.addAttribute("userEditForm", userEditForm);
		
		return "user/edit";
//		ユーザー編集ページ
	}
	
	@PostMapping("/update")
	public String update( @ModelAttribute @Validated UserEditForm userEditForm, BindingResult bindingResult, RedirectAttributes reditectAttributes) {
		
		if(userService.isEmailChanged(userEditForm) && userService.isEmailRegistered(userEditForm.getEmail())){
			
			FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email","すでに登録済みのメールアドレスです");
			
			bindingResult.addError(fieldError);
		}
		
		if(bindingResult.hasErrors()) {
			return "/user/edit";
		}
		
		userService.update(userEditForm);
		
		reditectAttributes.addFlashAttribute("successMessage", "会員情報を更新しました");
		return "redirect:/user";
//		リダイレクト先はUserControllerの＠RequestMapping(use)
		
		
	}
	

}
