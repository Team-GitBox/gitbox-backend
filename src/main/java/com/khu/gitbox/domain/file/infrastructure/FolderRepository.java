package com.khu.gitbox.domain.file.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khu.gitbox.domain.file.entity.Folder;

public interface FolderRepository extends JpaRepository<Folder, Long> {
}
