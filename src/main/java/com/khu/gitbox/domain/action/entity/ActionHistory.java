package com.khu.gitbox.domain.action.entity;

import com.khu.gitbox.domain.action.ActionHistoryDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Column(name = "file_id", nullable = false)
    private Long fileId;

    @Column(name = "workspace_id", nullable = false)
    private Long workspaceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private Action action;

    @Builder
    public static ActionHistoryDto toDto(ActionHistory actionHistory) {
        return ActionHistoryDto.builder()
                .name(actionHistory.name)
                .fileName(actionHistory.name)
                .action(actionHistory.action)
                .build();
    }
}
