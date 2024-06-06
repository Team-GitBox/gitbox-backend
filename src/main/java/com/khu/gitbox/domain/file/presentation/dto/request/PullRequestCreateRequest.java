package com.khu.gitbox.domain.file.presentation.dto.request;

public record PullRequestCreateRequest(
        String pullRequestTitle,
        String pullRequestMessage
) {
}
