package com.khu.gitbox.domain.workspace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "workspace_member")
@Getter
@Setter
@NoArgsConstructor
public class WorkspaceMember {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long memberId;

	@Column(name = "workspace_id")
	private Long workspaceId;

	@Builder
	public WorkspaceMember(Long memberId, Long workspaceId) {
		this.memberId = memberId;
		this.workspaceId = workspaceId;
	}
}
