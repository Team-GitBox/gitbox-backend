package com.khu.gitbox.domain.file.entity;

public enum FileStatus {
    // 대기 (파일 업데이트 시)
    PENDING,
    // 승인 및 첫 업로드
    APPROVED,
    // 거절
    REJECTED
}
