package com.khu.gitbox.domain.file.infrastructure;

import com.khu.gitbox.domain.file.entity.File;
import com.khu.gitbox.domain.file.entity.FileTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    void deleteByFolderId(Long folderId);

    @Query("SELECT f FROM File f WHERE f.parentFileId = :parentFileId AND f.status = 'PENDING' AND f.isDeleted = FALSE")
    Optional<File> findPendingFile(Long parentFileId);

    @Query("SELECT f FROM File f WHERE f.rootFileId = :rootFileId AND f.isDeleted = FALSE ORDER BY f.createdAt")
    List<File> findAllByRootFileId(Long rootFileId);

    @Query("SELECT f FROM File f WHERE f.folderId = :folderId AND f.isLatest = true AND f.isDeleted = FALSE ORDER BY f.name ASC")
    List<File> findLatestByFolderId(Long folderId);

    @Query("SELECT f FROM File f WHERE f.workspaceId = :workspaceId AND f.isDeleted = FALSE ORDER BY f.name ASC")
    List<File> findAllByWorkspaceId(Long workspaceId);

    @Query("SELECT f FROM File f WHERE f.workspaceId = :workspaceId AND f.tag = :tag AND f.isDeleted = FALSE ORDER BY f.name ASC")
    List<File> findAllByTag(Long workspaceId, FileTag tag);

    @Query("SELECT f FROM File f WHERE f.folderId = :folderId AND f.name = :name AND f.isDeleted = FALSE")
    Optional<File> findByFolderIdAndName(Long folderId, String name);

    //휴지통에 버린 파일 목록 조회
    @Query("SELECT f FROM File f WHERE f.workspaceId = :workspaceId AND f.isDeleted = TRUE ORDER BY f.name ASC")
    List<File> findAllByWorkspaceIdTrash(Long workspaceId);

    @Query("SELECT f FROM File f WHERE f.workspaceId = :workspaceId AND f.name LIKE %:keyword% AND f.isDeleted = FALSE ORDER BY f.name ASC")
    List<File> findAllByKeyword(Long workspaceId, String keyword);

    @Modifying
    @Query("DELETE FROM File f WHERE f.rootFileId = :rootFileId AND f.isDeleted = TRUE")
    void deleteByRootFileId(Long rootFileId);

    @Query("SELECT f FROM File f WHERE f.rootFileId = :rootFileId AND f.isDeleted = TRUE ORDER BY f.version DESC")
    List<File> findAllByRootFileIdTrash(Long rootFileId);

}
