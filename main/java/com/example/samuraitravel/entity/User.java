package com.example.samuraitravel.entity;



import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Table(name="users")
@Entity
@Data
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="name")	
	private String name;
	
	@Column(name="furigana")
	private String furigana;
	
	@Column(name="postal_code")
	private String postalCode;
	
	@Column(name="address")
	private String address;
	
	@Column(name="phone_number")
	private String phoneNumber;
	
	@Column(name="email")
	private String email;
	
	@Column(name="password")
	private String password;
	
	@ManyToOne
//	１人のユーザーは１つのロールに属するが、１つのロールは多数のユーザーが所持できる
	@JoinColumn(name="role_id")
//	外部キー
	private Role role;
	
	@Column(name="enabled")
	private Boolean enabled;
	@Column(name="created_at", insertable = false,updatable=false)
	private Timestamp createdAt;
//	insertの時間が自動挿入される
	
	@Column(name="updated_at", insertable = false,updatable=false)
	private Timestamp updatedAt;
//	updateの時間が自動挿入される
	
	

}
