package com.example.samuraitravel.event;

import org.springframework.context.ApplicationEvent;

import com.example.samuraitravel.entity.User;

import lombok.Getter;

@Getter
//外部のLisnerクラスが情報を取得できるようにゲッターを定義する。
public class SignupEvent extends ApplicationEvent {
//	基本的にEventクラスはApplicationEventクラス(springframework提供)を継承する。
//	ApplicationEventはイベントを作成するための基本的なクラスで、イベントのソース（発生源）などを保持する。
//	SignupEventクラスの役割２つ: イベント発生をListnerクラスに知らせる。イベントに関する情報（会員登録ユーザー情報(User)・リクエストを受けたURL(requestUrl)を保持する。
	
	
	private User user;
	private String requestUrl;
	
	public SignupEvent(Object source, User user, String requestUrl) {
		super(source);
//		親クラスのコンストラクたを呼び出し、イベントの発生源(source)を渡す。
//		イベントの発生源(soruce)=publisher クラスのインスタンスのこと
		
		this.user = user;
		this.requestUrl = requestUrl;
	}

}
