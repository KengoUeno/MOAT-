package com.example.samuraitravel.entity;



import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="houses")
//houseというnameのテーブルにマッピング
@Data
public class House {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	auto-incrementを指定したカラムに付与、idを昇順で自動生成する
	@Column(name="id")
	private Integer id;
	
	@Column(name="name")
	private String name;
	
	@Column(name ="image_name")
	private String imageName;
	
	@Column(name="description")
	private String description;
	
	@Column(name ="price")
	private Integer price;
	
	@Column(name ="capacity")
	private Integer capacity;
	
	@Column(name= "postal_code")
	private String postalCode;
	
	@Column(name="address")
	private String address;
	
	@Column(name="phone_number")
	private String phoneNumber;
	
	@Column(name="created_at", insertable = false,updatable=false)
//	insertableとupdatableをflase指定で値を手動で管理できなくなり、DBが自動で更新するようになる。
//	schema.sqlに記載したdafoult current timestampにより、dbがわに設定しらデフォルト値(current timestamp)が自動挿入される
	private Timestamp createdAt;
	
	@Column(name="updated_at", insertable=false, updatable=false)
	private Timestamp updateAt;

}
