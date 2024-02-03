package com.example.samuraitravel.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class HouseRegisterForm {
	@NotBlank(message="民泊名を入力してください")
	private String name;
	
	private MultipartFile imageFile;
//	Multiparfile= springbootが提供するインターフェース、フォームから送られてくるimageFileをアプリ側で処理できるようになる
	
	@NotBlank(message="説明を入力してください")
	private String Description;
	
	@NotNull(message="宿泊料金を入力してください")
	@Min(value=1, message="料金は１円以上にせっていしてください")
	private Integer price;
	
	@NotNull(message="定員を入力してください")
	@Min(value=1, message="定員は１人以上に設定してください")
	private Integer capacity;
	@NotBlank(message="郵便番号を入力してください")
	private String postalCode;
	
	@NotBlank(message="住所を入力してください")
	private String address;
	
	@NotBlank(message="電話番号を入力してください")
	private String phoneNumber;
	
	
	
	

}
