package com.example.samuraitravel.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.Role;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.SignupForm;
import com.example.samuraitravel.form.UserEditForm;
import com.example.samuraitravel.repository.RoleRepository;
import com.example.samuraitravel.repository.UserRepository;

@Service

public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public User create(SignupForm signupForm) {
		//基本的には民泊登録とほとんど同じ
		User user = new User();

		Role role = roleRepository.findByName("ROLE_GENERAL");
		//今回は一般ユーザーのみの登録なのでroleRepositoryを通してroleエンティティからrole_generalのデータをもってくる。

		user.setName(signupForm.getName());
		user.setFurigana(signupForm.getFurigana());
		user.setPostalCode(signupForm.getPostalCode());
		user.setAddress(signupForm.getAddress());
		user.setPhoneNumber(signupForm.getPhoneNumber());
		user.setEmail(signupForm.getEmail());
		user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
		//setする際にpasswordEncodeでパスワードをハッシュ化する
		user.setRole(role);
		user.setEnabled(false);

		return userRepository.save(user);

	}

	public boolean isEmailRegistered(String email) {
		//		同一メアドの登録を防ぐため、挿入されたメアドがdbにないか調べる
		User user = userRepository.findByEmail(email);

		if (user != null) {
			return true;
		}
		return false;
	}

	public boolean isSamePassword(String password, String passwordConfirmation) {
		return password.equals(passwordConfirmation);
	}

	@Transactional
	public void enableUser(User user) {
		//メール認証ページ(http://ドメイン/signup/verify?token)で認証が成功した際にdbのenabledカラムのfalseをtrueに変換する処理
		user.setEnabled(true);
		userRepository.save(user);
	}

	@Transactional
	public void update(UserEditForm userEditForm) {

		User user = userRepository.getReferenceById(userEditForm.getId());
		//		更新したいユーザー情報の取得

		user.setName(userEditForm.getName());
		user.setFurigana(userEditForm.getFurigana());
		user.setPostalCode(userEditForm.getPostalCode());
		user.setAddress(userEditForm.getAddress());
		user.setPhoneNumber(userEditForm.getPhoneNumber());
		user.setEmail(userEditForm.getEmail());

		userRepository.save(user);
	}

	public boolean isEmailChanged(UserEditForm userEditForm) {
		
		User currentUser =  userRepository.getReferenceById(userEditForm.getId());
		
		return !userEditForm.getEmail() .equals(currentUser.getEmail());
	}

}
