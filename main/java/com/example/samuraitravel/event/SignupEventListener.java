package com.example.samuraitravel.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.service.VerificationTokenService;

@Component
//ListnerクラスのインスタンスがDIコンテナに登録される。これ後述する@EventListnerがついたメソッドをSpring Boot側が自動検出し、イベント発生時に実行する
public class SignupEventListener {
//	イベント発生時に行う処理内容を記述すクラス
	private final VerificationTokenService verificationTokenService;
	private final JavaMailSender javaMailSender;
	
	
	public SignupEventListener(VerificationTokenService verificationTokenService, JavaMailSender mailSender) {
		this.verificationTokenService=verificationTokenService;
		this.javaMailSender = mailSender;
	}
	
	@EventListener
	
//	イベント発生時に実行するメソッドにつけるアノテーション。
//	どのイベントの発生時かを指定するため、下記メソッドの引数にイベントのクラスを設定する
//	今回の場合、signupEventクラスからイベント発生の通知を受けたときにonSignupEventメソッドが実行される
	private void onSignupEvent(SignupEvent signupEvent) {
		User user = signupEvent.getUser();
		String token=UUID.randomUUID().toString();
//		UUIDをString型に変更して、tokenフィールドに格納
		
		verificationTokenService.create(user,token);
//		dbテーブル"verification_tokens"に新しいレコードを作成する処理を行うメソッドを呼び出す。
		String recipientAddress = user.getEmail();
//		登録ボタンをポチった新規ユーザーのメアド情報
		
		String subject = "メール認証";
		
		String confirmationUrl = signupEvent.getRequestUrl()+ "/verify?token="+token;
//		http://ドメイン名//signup/verify?token=生成したトークン
		
		String message = "以下のリンクをクリックして会員登録を完了してください";
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
//		springFrameworkが提供するメッセージ作成機能。
		
		mailMessage.setTo(recipientAddress); //送信先のメアドを設定
		mailMessage.setSubject(subject); //メールの件名
		mailMessage.setText(message + "\n"+confirmationUrl); //メール本文+認証用のURLのリンク
		
		javaMailSender.send(mailMessage);
//		javaのメール送信処理を行う機能
//		springbootプロジェクト作成時に依存関係を指定したやつ
		
		
	}

}
