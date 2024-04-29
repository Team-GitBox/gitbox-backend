package com.khu.gitbox.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private final HttpStatus errorCode;

	public CustomException(HttpStatus errorCode, String errorMessage) {
		super(errorMessage);
		this.errorCode = errorCode;
	}
}
