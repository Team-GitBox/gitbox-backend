package com.khu.gitbox.domain.pullRequest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "pull_request_comment")
public class PullRequestComment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "comment")
	private String comment;

	@Column(name = "is_approved", nullable = false)
	private Boolean isApproved;

	@Column(name = "reviewr_id", nullable = false)
	private Long reviewerId;

	@Column(name = "pull_request_id", nullable = false)
	private Long pullRequestId;
}
