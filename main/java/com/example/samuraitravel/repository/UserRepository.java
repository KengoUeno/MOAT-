package com.example.samuraitravel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	public User findByEmail(String email);
//	メールアドレスでユーザーを検索する抽象メソッド
	
	public Page<User> findByNameLikeOrFuriganaLike(String nameKeyword,String furiganaKeyword,Pageable pageable);
//	氏名、またはふりがなで会員を検索できるようにする。
	
//	and や　or　を使うことでSQLのAND検索やOR検索ができる
//	Like カラム名の末尾につけることでSQLの LIKE句と同様のクエリを実行できるようにしている
}
