package com.khu.gitbox.domain.file.presentation.dto.request;

import com.khu.gitbox.domain.file.entity.FileTag;

import jakarta.validation.constraints.NotBlank;

public record FileUpdateRequest(
	@NotBlank(message = "파일 이름을 1자 이상 입력해주세요.")
	String name,
	
	FileTag tag
) {
}
