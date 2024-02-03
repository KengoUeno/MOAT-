package com.example.samuraitravel.entity;



import java.sql.Timestamp;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name="reservations")
@Entity
public class Reservation {

	@Column(name ="id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JoinColumn(name="house_id")
	@ManyToOne
	private House house;
//	1つの民泊は複数の予約をモテるのでmanytoone

	@JoinColumn(name="user_id")
	@ManyToOne
	private User user;
//	１人のユーザーは複数の予約をモテるのでmanytoone

	@Column(name = "checkin_date")
	private LocalDate checkinDate;

	@Column(name="checkout_date")
	private LocalDate checkoutDate;

	@Column(name="number_of_people")
	private Integer numberOfPeople;

	@Column(name="amount")
	private Integer amount;

	@Column(name="created_at", insertable=false, updatable=false)
	private Timestamp createdAt;

	@Column(name="updated_at", insertable=false, updatable=false)
	private Timestamp updatedAt;
}
