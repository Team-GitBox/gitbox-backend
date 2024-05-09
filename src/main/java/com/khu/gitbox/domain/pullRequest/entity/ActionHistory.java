package com.khu.gitbox.domain.pullRequest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "action_history")
public class ActionHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Column(name = "workspace_id", nullable = false)
	private Long workspaceId;

	@Enumerated(EnumType.STRING)
	@Column(name = "action", nullable = false)
	private Action actionHistory;

}
