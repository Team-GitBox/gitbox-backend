package com.khu.gitbox.domain.action.entity;

import com.khu.gitbox.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "action_history")
public class ActionHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "file_id", nullable = false)
    private Long fileId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "workspace_id", nullable = false)
    private Long workspaceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private Action action;

    public ActionHistory(Long memberId, String memberName, Long fileId, String fileName, Long workspaceId,
                         Action action) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.fileId = fileId;
        this.fileName = fileName;
        this.workspaceId = workspaceId;
        this.action = action;
    }
}
