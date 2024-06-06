package com.khu.gitbox.domain.pullRequest.entity;

import com.khu.gitbox.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "pull_request_comment")
public class PullRequestComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment")
    private String comment;

    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved;

    @Column(name = "reviewer_id", nullable = false)
    private Long reviewerId;

    @Column(name = "pull_request_id", nullable = false)
    private Long pullRequestId;

    @Builder
    public PullRequestComment(String comment, Boolean isApproved, Long reviewerId, Long pullRequestId) {
        this.comment = comment;
        this.isApproved = isApproved;
        this.reviewerId = reviewerId;
        this.pullRequestId = pullRequestId;
    }
}
