package com.khu.gitbox.domain.file.presentation.dto;

public record PullRequestCreateRequest(
	String title,
	String message
) {
}
