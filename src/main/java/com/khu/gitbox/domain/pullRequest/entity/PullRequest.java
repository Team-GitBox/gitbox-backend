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
@Table(name = "pull_request")
public class PullRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "writer_id", nullable = false) // 연관관계
    private Long writerId;

    @Column(name = "file_id", nullable = false) // 연관관계
    private Long fileId;

    @Builder
    public PullRequest(String title, String message, Long writerId, Long fileId) {
        this.title = title;
        this.message = message;
        this.writerId = writerId;
        this.fileId = fileId;
    }
}
