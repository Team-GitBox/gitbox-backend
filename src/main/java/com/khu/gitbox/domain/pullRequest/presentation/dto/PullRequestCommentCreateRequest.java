package com.khu.gitbox.domain.pullRequest.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PullRequestCommentCreateRequest {
	@NotBlank
	private String comment;
	@NotNull
	private Boolean isApproved;
}
