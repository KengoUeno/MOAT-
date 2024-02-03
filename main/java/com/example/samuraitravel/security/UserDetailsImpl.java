package com.example.samuraitravel.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.samuraitravel.entity.User;

public class UserDetailsImpl implements UserDetails {
//	ユーザー情報保持、認証、認可を担当

	//	 UserDetailsインターフェース　＝spring securityが提供するインターフェースでユーザー情報を保持する役割がある。以下のメソッドが定義されている。
	//Collection <? extends GrantedAuthority> getAuthorities()権限を返す, String getPassword()パスワードを返す, String getUsername()　ユーザー名を返す, boolean isAccountNotExpired()アカウントの有効期限,
	//boolean isAccountNotLocked()アカウントがロックされてないか, boolean isCredentialsNonExpired()アカウントの認証情報が有効期限内か, boolean isEnabled()アカウントが有効か,
	//インターフェースなので、すべてoverrindeで実装しなければならない。

	private final User user;
	private final Collection<GrantedAuthority> authorities;
	//	CollectionのGrantedAuthority型のフィールド名authorities

	public UserDetailsImpl(User user, Collection<GrantedAuthority> authorities) {
		//上記の定数２つを引数にとるコンストラクタ/セッター
		this.user = user;
		this.authorities = authorities;
		//ユーザーとその権限を格納する
	}

	public User getUser() {
		//ログイン中のユーザーの会員情報を表示、編集する機能や予約機能で使用する予定。
		//独自メソッドなのでoverrideはしない
		return user;

	}
	
	

	// ハッシュ化済みのパスワードを返す
	@Override
	public String getPassword() {
		return user.getPassword();
	}
	// ログイン時に利用するユーザー名（メールアドレス）を返す
	@Override
	public String getUsername() {
		return user.getEmail();
	}

	// ロールのコレクションを返す

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//	   権限を返すメソッド
		//GrantedAuthority ユーザーに割り当てられた権限を表すインンターフェース、spring secury が提供
		//	   collection＝データの集まり
		//	   <? extends GrantedAuthoritu> = GrantedAuthorityを継承して実装した全てクラス、またはそのオブジェクトのコレクション のインスタンスの型名？

		return authorities;
	}

	// アカウントが期限切れでなければtrueを返す
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// ユーザーがロックされていなければtrueを返す
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// ユーザーのパスワードが期限切れでなければtrueを返す
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// ユーザーが有効であればtrueを返す
	//   メール認証機能は今後の章で作成するので、ユーザーの有効性チェックは必要です
	@Override
	public boolean isEnabled() {
		
		return user.getEnabled();
	}
	//上記４つのメソッドでtrueが帰った場合のみログインを許可する
	//   本アプリではアカウントやパスワードの期限、アカウントのロックといった機能は作成しません。よって、それらのメソッドは必ずtrueを返すようにしています。
}
