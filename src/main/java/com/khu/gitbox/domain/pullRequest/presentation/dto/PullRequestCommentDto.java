package com.khu.gitbox.domain.pullRequest.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PullRequestCommentDto {
    private String comment;
    private Boolean isApproved;
    private Long reviewerId;
}