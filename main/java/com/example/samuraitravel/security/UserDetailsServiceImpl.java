package com.example.samuraitravel.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
// Spring security が提供するインターフェース
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.UserRepository;

@Service

public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;
//	JpaのCRUD処理+独自のfindByEmailメソッドを提供する

	public UserDetailsServiceImpl(UserRepository userRepository) {
//		コンストラクタ兼セッター的な
		this.userRepository = userRepository;

	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//		多分、loginフォームから受け取っとユーザーネーム(email)がここにくる
		// UserDetailsンターフェースで定義されている抽象メソッドはloadUserByUsername()のみです。
		//   よってUserDetailsServiceImplクラスでは、このloadUserByUsername()を上書きし、具体的な処理内容を作成するだけでOKです。
		//		loadUserBYUsername フォームから送信されたメールアドレスに一致するユーザーとその権限を取得しUserDetailsImplクラスのコンストラクタに渡してインスタンスを生成すr。

		try {
			User user = userRepository.findByEmail(email);
			//独自メソッドfindByEmailでフォームに送られてきたメアドを元にユーザー情報を取得

			String userRoleName = user.getRole().getName();
			//取得したユーザー情報から名前と権限を取り出しでuserRole
			Collection<GrantedAuthority> authorities = new ArrayList<>();

			authorities.add(new SimpleGrantedAuthority(userRoleName));

			return new UserDetailsImpl(user, authorities);
//			user=メアドを元にdbから取得したユーザー情報全体が入っている
//			authorities=ユーザの名前と権限が入っている。
//			上記２つを引数にとるUserDetailImクラスを元に,userDetailImpインスタンスを作成。
//			UserDetailImpクラスのコンストラクタに渡す

		} catch (Exception e) {

			throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");

		}

	}

}
