package com.example.demo.domain.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.domain.user.model.MUser;
import com.example.demo.domain.user.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserService service;

	public UserDetailsServiceImpl(@Lazy UserService service) {
		this.service = service;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// ユーザー情報取得
		MUser loginUser = service.getLoginUser(username);
		
		// ユーザーが存在しない場合
		if(loginUser == null) {
			throw new UsernameNotFoundException("user not found");
		}

		// 権限List作成
		GrantedAuthority authority = new SimpleGrantedAuthority(loginUser.getRole());
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(authority);

		// UserDetails生成
		UserDetails userDetails = (UserDetails) new User(loginUser.getUserId(),loginUser.getPassword(),authorities);

		return userDetails;
	}
}