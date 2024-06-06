package com.khu.gitbox.domain.file.infrastructure;

import com.khu.gitbox.domain.file.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    Optional<Folder> findByIdAndWorkspaceId(Long folderId, Long workspaceId);

    List<Folder> findAllByParentFolderId(Long parentFolderId);

    Optional<Folder> findByParentFolderIdAndName(Long parentFolderId, String folderName);
}
