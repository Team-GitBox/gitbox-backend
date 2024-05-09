package com.khu.gitbox.domain.pullRequest.presentation.dto;

import com.khu.gitbox.domain.pullRequest.entity.PullRequestComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class PullRequestDto {
    private String title;
    private String message;
    private String writer;
    private String fileUrl;

    private List<PullRequestComment> commentDtoList;


}
