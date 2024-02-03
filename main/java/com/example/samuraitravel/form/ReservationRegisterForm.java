package com.example.samuraitravel.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationRegisterForm {

	private Integer houseId;
//	宿泊する宿のid
	private Integer userId;
//	宿泊者のid
	private String checkinDate;
//	チェックインの日
	private String checkoutDate;
//	チェックアウトの日
	private Integer numberOfPeople;
//	宿泊人数
	private Integer amount;
//	合計金額
}

//バリデーションはreservationInputFormで行うので、ここは単純にデータを格納するのみ
