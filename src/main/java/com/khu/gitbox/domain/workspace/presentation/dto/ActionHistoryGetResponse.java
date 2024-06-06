package com.khu.gitbox.domain.workspace.presentation.dto;

import com.khu.gitbox.domain.action.entity.Action;
import com.khu.gitbox.domain.action.entity.ActionHistory;

import java.time.LocalDateTime;

public record ActionHistoryGetResponse(
        Long id,
        Long actorId,
        String actorName,
        Long targetFileId,
        String targetFileName,
        Action action,
        LocalDateTime createdAt
) {
    public static ActionHistoryGetResponse of(ActionHistory actionHistory) {
        return new ActionHistoryGetResponse(
                actionHistory.getId(),
                actionHistory.getMemberId(),
                actionHistory.getMemberName(),
                actionHistory.getFileId(),
                actionHistory.getFileName(),
                actionHistory.getAction(),
                actionHistory.getCreatedAt()
        );
    }
}
