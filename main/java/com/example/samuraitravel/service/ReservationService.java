package com.example.samuraitravel.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Reservation;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReservationRepository;
import com.example.samuraitravel.repository.UserRepository;

import jakarta.transaction.Transactional;

//コントローラーからバリデーションなどの命令を受け取り、それを実行する処理内容を書く

@Service
public class ReservationService {
	private final HouseRepository houseRepository;
	private final UserRepository userRepository;
	private final ReservationRepository reservationRepository;


	public ReservationService(HouseRepository houseRepository, UserRepository userRepository,ReservationRepository reservationRepository) {
		this.houseRepository = houseRepository;
		this.userRepository = userRepository;
		this.reservationRepository=reservationRepository;
	}

	public boolean isWithinCapacity(Integer numberOfPeople, Integer capacity) {
		//		定員確認
		return numberOfPeople <= capacity;
	}

	public Integer calculateAmount(LocalDate checkinDate, LocalDate checkoutDate, Integer price) {
		//	料金計算
		long numberOfNights = ChronoUnit.DAYS.between(checkinDate, checkoutDate);
		//long＝大きな整数型を扱うための整数データ型
		//chronoUnit.DAYS.between 二つの日付の間の日数を計算する
		int amount = price * (int) numberOfNights;

		return amount;
	}

	@Transactional
	public void create(Map<String,String> paymentIntentObject) {
//		予約確認フォームの内容をdbに保存する処理
		//paymentIntentObject=支払い情報のメタデータが格納されている。

		Reservation reservation = new Reservation();
		//	新しいレコードを作るためにエンティティからインスタンスを生成
		Integer houseId = Integer.valueOf(paymentIntentObject.get("houseId"));
		Integer userId = Integer.valueOf(paymentIntentObject.get("userId"));


		House house = houseRepository.getReferenceById(houseId);
//		reservationRegisterFormのhouserID(Integer型)をhouse型に治す必要がある。
		User user = userRepository.getReferenceById(userId);
//		reservationRegisterFormのuserID(Integer型)をuser型に治す必要がある。
		LocalDate checkinDate = LocalDate.parse(paymentIntentObject.get("checkinDate"));
//		reservationRegisterFormのcheckindate(String型)をLocalDate型に治す必要がある。
		LocalDate checkoutDate = LocalDate.parse(paymentIntentObject.get("checkoutDate"));
//		reservationRegisterFormのcheckoutDate(String型)をLocalDate型に治す必要がある。
		Integer numberOfPeople = Integer.valueOf(paymentIntentObject.get("checkoutDate"));
		Integer amount = Integer.valueOf(paymentIntentObject.get("amount"));

		//ReservationRegisterFormのデータをentitiー＞dbに格納するためには、全てのデータ型をエンティティに記載されているデータ型に統一する必要がある。

		reservation.setHouse(house);
		reservation.setUser(user);
		reservation.setCheckinDate(checkinDate);
		reservation.setCheckoutDate(checkoutDate);
		reservation.setNumberOfPeople(numberOfPeople);
		reservation.setAmount(amount);

		reservationRepository.save(reservation);

	}

}
