package com.example.samuraitravel.security;





 import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



 @Configuration
// 設定用のクラスとして機能するようになる。メソッドに@Beanアノテーションをつけるために必要
 @EnableWebSecurity
// Spring Securityによるセキュリティ機能を有効にし
 @EnableMethodSecurity
// メソッドレベルでのセキュリティ機能を有効
public class WebSecurityConfig {

//	 認可やログインURLの各種設定を行うクラス
     @Bean
     //public削除してみました。後々エラーでたらもどします。
       public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    	 @Beanをつけることで、メソッドの戻り値（インスタンス）がDIコンテナに登録される。Spring BootではこのDIコンテナに登録されたインスタンスのことを、Beanと呼びます。
//    	 securityfilterchaim=誰にどのページへのアクセスを許可するかの設定、ログインの設定、ログアウトの設定

        http
		            .authorizeHttpRequests((requests) -> requests
		//            		だれにどのページへのアクセスを許可するか
				                .requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**", "/", "/signup/**","/houses", "/houses/{id}","/stripe/webhook").permitAll()
				                // 全てにすべてのユーザーにアクセスを許可するURL
				                .requestMatchers("/admin/**").hasRole("ADMIN")
				//                template/adminフォルダ内にある全てのフォルダは管理者にのみアクセスを許可するURL, 民泊の登録、編集、削除
				                .anyRequest().authenticated()
				                // 上記以外のURLはログインが必要（会員または管理者のどちらでもOK）
		             )



		             .formLogin((form) -> form
		//            		 ログインの設定
				                 .loginPage("/login")              // ログインページのURL　http://localhose:8080/login

				                 .loginProcessingUrl("/login")     // ログインフォームの送信先URL 上記URLのファイルtemplate/auth/login.html内の <form th:action="@{/login}" method="post">
				                 .defaultSuccessUrl("/?loggedIn")  // ログイン成功時のリダイレクト先URL
				                 .failureUrl("/login?error")       // ログイン失敗時のリダイレクト先URL.  http://localhost:8080/login?error ログインページ+エラーメッセージ(param.error)
				                 .permitAll()
		//		                 全てのユーザーにアクセス権限あり
		             )



		             .logout((logout) -> logout
		//            		 ログアウトの設定
				               .logoutSuccessUrl("/?loggedOut")  // ログアウト時のリダイレクト先URL
				               .permitAll()
		           )
        
        
        			.csrf().ignoringRequestMatchers("/stripe/webhook");
        
     

       return http.build();
//       httpSecurityオブジェクトを生成し、戻り値として返す


   }





   @Bean
   //public削除してみました。後々エラーでたらもどします。
     public PasswordEncoder passwordEncoder() {
//	   パスワードのハッシュアルゴリズムを設定する


       return new BCryptPasswordEncoder();
//       BCryptはパスワード用のハッシュ値を生成してくれる強力なハッシュアルゴリズム。


   }
 }











