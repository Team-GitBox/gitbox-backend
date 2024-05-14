package com.khu.gitbox.util;

import org.springframework.security.core.context.SecurityContextHolder;

import com.khu.gitbox.auth.application.UserDetailsImpl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityContextUtil {
	public static Long getCurrentMemberId() {
		final UserDetailsImpl userDetails = (UserDetailsImpl)SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal();
		return userDetails.getId();
	}
}
