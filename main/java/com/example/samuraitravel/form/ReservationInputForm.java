package com.example.samuraitravel.form;


import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationInputForm {
//reservations/show.htmlの予約フォームに入力される値の雛形


	@NotBlank(message="チェンクイン日とチェックアウト日を選択してください。")
	private String fromCheckinDateToCheckoutDate;

	@NotNull(message="宿泊人数を入力してください")
	@Min(value=1, message="宿泊人数は１人以上に設定してください")
	private Integer numberOfPeople;



	public LocalDate getCheckinDate() {
		String[] checkinDateAndCheckoutDate=getFromCheckinDateToCheckoutDate().split(" から ");
		return LocalDate.parse(checkinDateAndCheckoutDate[0]);
	}

	public LocalDate getCheckoutDate() {
		String[] checkinDateAndCheckoutDate = getFromCheckinDateToCheckoutDate().split(" から ");
		return LocalDate.parse(checkinDateAndCheckoutDate[1]);
	}

//	split()文字列を分割するメソッド、引数にはその分割位置を特定するための文字列が入る。
//	parse()引数に入るString型変数をDate型にへんかんするメソッド。

}
