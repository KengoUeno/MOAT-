package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.entity.VerificationToken;
import com.example.samuraitravel.repository.VerificationTokenRepository;

@Service
public class VerificationTokenService {
	private final VerificationTokenRepository verificationTokenRepository;
	
	
	public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
		this.verificationTokenRepository = verificationTokenRepository;
	}
	
	@Transactional
	public void create(User user, String token) {
//		SignupEventListnerクラスから引数を受け取り、dbのテーブル"verification_tokens"に新しいレコードを登録する処理
		VerificationToken verificationToken = new VerificationToken();
//		エンティティverificationtokenからインスタンスを生成
		
		verificationToken.setUser(user);
		verificationToken.setToken(token);
		
		verificationTokenRepository.save(verificationToken);
	}
	
	public VerificationToken getVerificationToken(String token) {
		return verificationTokenRepository.findByToken(token);
		
	}

}
