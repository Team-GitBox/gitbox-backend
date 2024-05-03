package com.khu.gitbox.domain.file.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khu.gitbox.domain.file.entity.File;

public interface FileRepository extends JpaRepository<File, Long> {
}
