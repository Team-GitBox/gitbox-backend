package com.khu.gitbox.domain.file.entity;

import com.khu.gitbox.common.BaseEntity;
import com.khu.gitbox.common.exception.CustomException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "file")
public class File extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "url", nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "tag")
    private FileTag tag;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private FileType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FileStatus status;

    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "is_latest", nullable = false)
    private boolean isLatest;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Column(name = "writer_id", nullable = false)
    private Long writerId;

    @Column(name = "workspace_id", nullable = false)
    private Long workspaceId;

    @Column(name = "folder_id", nullable = false)
    private Long folderId;

    @Column(name = "parent_file_id")
    private Long parentFileId;

    @Column(name = "root_file_id")
    private Long rootFileId;

    @Builder
    File(
            String name,
            Long size,
            String url,
            FileType type,
            FileStatus status,
            Long version,
            boolean isLatest,
            Long writerId,
            Long workspaceId,
            Long folderId,
            Long parentFileId,
            Long rootFileId
    ) {
        this.name = name;
        this.size = size;
        this.url = url;
        this.type = type;
        this.status = status;
        this.version = version;
        this.isLatest = isLatest;
        this.writerId = writerId;
        this.workspaceId = workspaceId;
        this.folderId = folderId;
        this.parentFileId = parentFileId;
        this.rootFileId = rootFileId;
    }

    public void updateRootFileId(Long id) {
        this.rootFileId = id;
    }

    public void updateFile(String name, FileTag tag) {
        this.name = name;
        this.tag = tag;
    }

    public void updateStatus(FileStatus status) {
        this.status = status;
    }

    public void updateTag(FileTag tag) {
        this.tag = tag;
    }

    public void delete() {
        if (this.isDeleted) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 삭제된 파일입니다.");
        }
        this.isDeleted = true;
    }

    public void restore() {
        if (!this.isDeleted) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 복원된 파일입니다.");
        }
        this.isDeleted = false;
    }
}
