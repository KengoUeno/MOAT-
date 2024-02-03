package com.example.samuraitravel.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.samuraitravel.entity.User;

@Component
//DIコンテナに登録し、呼び出し元のクラス（今回はコントローラー）に対して依存性の注入を行える様にする。
public class SignupEventPublisher {
//	イベントを発行するくらす
//	イベント発行をSignupEventクラスが通知受け取り、SignupEventクラスからListnerクラスにイベント発行通知。
//	今回の場合、authControllerのsignup()メソッドの中で呼び出して使う。
	private final ApplicationEventPublisher applicationEventPublisher;
//	ApplicationEventPublisher ＝イベント発行機能をカプセル化しているインターフェース

	public SignupEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}
	
	
	public void publishSignupEvent(User user, String requestUrl) {
		applicationEventPublisher.publishEvent(new SignupEvent(this,user,requestUrl));
//		publishEvent=ApplicationEventPublisher が提供するイベント発行メソッド引数に発行するイベントクラス(SignupEvent)のインスタンスを渡す。
//		SignupEventインスタンスの引数には、this. =自分自身(SignupEventPublisherくらす)を入れる。
//	受け取り側のSignupEventの第一引数(Object source)にthisがはいる。つまり、イベントの発生源はSignupEventPublisherであることを知らせる
	}
}
