package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {
	
	@Autowired
	private UserDetailsService userDetailsService;

	// パスワードの暗号化
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/** セキュリティの対象外を設定 */
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers(
				new AntPathRequestMatcher("/webjars/**"), new AntPathRequestMatcher("/css/**"),
				new AntPathRequestMatcher("/js/**"), new AntPathRequestMatcher("/h2-console/**"));
		}

	/** セキュリティの各種設定 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		// ログイン不要ページの設定
		http
				.authorizeHttpRequests()
				.requestMatchers("/login").permitAll() //直リンクOK
				.requestMatchers("/user/signup").permitAll() //直リンクOK
				.requestMatchers("/user/signup/rest").permitAll() //直リンクOK
				.requestMatchers("/admin").hasAnyAuthority("ROLE_ADMIN")//権限制御
				.anyRequest().authenticated(); // それ以外は直リンクNG

		//ログイン処理
		http.formLogin()
				.loginProcessingUrl("/login")//ログイン処理のパス
				.loginPage("/login")//ログインページの指定
				.failureUrl("/login?error")//ログイン失敗時の遷移先
				.usernameParameter("userId")//ログインページのユーザーID
				.passwordParameter("password")//ログインページのパスワード
				.defaultSuccessUrl("/user/list", true);//成功後の遷移先
		
		//ログアウト処理
		http.logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutUrl("/logout")
		.logoutSuccessUrl("/login?logout");

		// CSRF対策を無効に設定（一時的）
		//http.csrf().disable();

		return http.build();
	}

    /** 認証の設定 */
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        PasswordEncoder encoder = passwordEncoder();
        // インメモリ認証
        /*
        auth
            .inMemoryAuthentication()
                .withUser("user") // userを追加
                    .password(encoder.encode("user"))
                    .roles("GENERAL")
                .and()
                .withUser("admin") // adminを追加
                    .password(encoder.encode("admin"))
                    .roles("ADMIN");
        */

        // ユーザーデータ認証
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(encoder);
    }
}
