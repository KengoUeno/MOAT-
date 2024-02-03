package com.example.samuraitravel.form;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;



@Data
public class SignupForm {
	
	@NotBlank(message="名前を入力してください")
	private String name;
	
	@NotBlank(message="フリガナを入力してください")
	private String furigana;
	
	@NotBlank(message="郵便番号を入力してください")
	private String postalCode;
	
	@NotBlank(message="住所を入力してください")
	private String address;
	
	@NotBlank(message="電話番号を入力してください")
	private String phoneNumber;
	
	@NotBlank(message="メールアドレスを入力してください")
	
	@Email(message="メールアドレスは正しい形式で入力してください")
	private String email;
	
	@NotBlank(message="パスワードを入力してください")
	@Length(min=8, message="パスワードは8文字以上にしてください")
	private String password;
	
	@NotBlank(message="確認用パスワードを入力してください")
	private String passwordConfirmation;
	
	
	

}
