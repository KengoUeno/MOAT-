package com.example.samuraitravel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.entity.VerificationToken;
import com.example.samuraitravel.event.SignupEventPublisher;
import com.example.samuraitravel.form.SignupForm;
import com.example.samuraitravel.service.UserService;
import com.example.samuraitravel.service.VerificationTokenService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {
	private final UserService userService;
	private final SignupEventPublisher signupEventPublisher;
	private final VerificationTokenService verificationTokenService;

	public AuthController(UserService userService, SignupEventPublisher signupEventPublisher,VerificationTokenService verificationTokenService) {
		this.userService = userService;
		this.signupEventPublisher = signupEventPublisher;
		this.verificationTokenService=verificationTokenService;
	}

	@GetMapping("/login")
	public String login() {

		return "auth/login";
		//		template/auth/login.htmlへ遷移
	}

	@GetMapping("/signup")
	public String signUp(Model model) {
		model.addAttribute("signupForm", new SignupForm());

		return "auth/signup";

	}

	@PostMapping("/signup")
//	会員登録開始から認証メールを送るまでの処理
	public String signup(@ModelAttribute @Validated SignupForm signupForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {

		if (userService.isEmailRegistered(signupForm.getEmail())) {
			FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです");
			//			FieldErrorクラスのインスタンスに渡す引数は（第１エラー内容格納オブジェクト、第２エラーを呼び出すフィールド名、第３メッセージ文)

			bindingResult.addError(fieldError);
		} //			bindigResultインターフェースが提供するaddErrorで独自のエラー内容を追加することができる。

		if (!userService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
			FieldError fieldError = new FieldError(bindingResult.getObjectName(), "password", "パスワードが一致しません");
			bindingResult.addError(fieldError);

		}

		if (bindingResult.hasErrors()) {
			//			bindingresultのエラーメッセージはフォームクラスでアノテーション(@notblankや@minなど)に記載したメッセージ。
			return "auth/signup";
		}

		//		userService.create(signupForm);
		//		redirectAttributes.addFlashAttribute("successMessage","会員登録が完了しました");

		User createUser = userService.create(signupForm);

		String requestUrl = new String(httpServletRequest.getRequestURL());
//		httpServletRequestインターフェース= HTTPリクエストに関する様々な情報を提供する。動的にURLを取得するので、環境が変化しても大丈夫
//		get.RequestURL=リクエストURL(https://ドメイン//signup)を取得
//		

		signupEventPublisher.publishSignupEvent(createUser, requestUrl);

		redirectAttributes.addFlashAttribute("successMessage",
				"ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、会員登録を完了してください");

		return "redirect:/";
		//		トップページ（template/index.html)に遷移

	}
	
	@GetMapping("/signup/verify")
	//認証用メールに添付したリンクhttp://ドメイン名//signup/verify?token=生成したトークンをポチったユーザーがここにくる
	public String verify(@RequestParam(name="token") String token,Model model) {
		VerificationToken verificationToken= verificationTokenService.getVerificationToken(token);
//		@Requestparam=(name=token)で、URL内のトークンを取得		
//		トークン情報から、 db(verificationtokenテーブル)に同じトークンを持つレコードを取得。
		if(verificationToken !=null) {
			User user=verificationToken.getUser();
//			verificationtokenテーブルのuseカラムは外部キーで、userテーブルとリンクしてるので、getUserで得た外部キー情報から、usersテーブルのレコード情報が取得できる。
			userService.enableUser(user);
//			enable=falseになっているユーザーを有効(true)にする処理
			
			String successMessage ="会員登録が完了しました";
			
			model.addAttribute("successMessage", successMessage);
			
		}else {
			String errorMessage ="トークンが向こうです";
			
			model.addAttribute("errorMessage",errorMessage);
		}
		return "auth/verify";
	}

}
