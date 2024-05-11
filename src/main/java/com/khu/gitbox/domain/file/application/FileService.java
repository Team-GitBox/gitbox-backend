package com.khu.gitbox.domain.file.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.file.entity.File;
import com.khu.gitbox.domain.file.entity.FileStatus;
import com.khu.gitbox.domain.file.entity.FileType;
import com.khu.gitbox.domain.file.infrastructure.FileRepository;
import com.khu.gitbox.s3.S3Service;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

	private static final Logger log = LoggerFactory.getLogger(FileService.class);
	private final FileRepository fileRepository;
	private final FolderService folderService;
	private final S3Service s3Service;

	// 새로운 파일 업로드
	public void uploadFile(Long workspaceId, Long folderId, MultipartFile multipartFile) {
		final String fileName = multipartFile.getOriginalFilename();
		final FileType fileType = FileType.from(getExtension(fileName).toUpperCase());

		final File newFile = File.builder()
			.name(fileName)
			.size(multipartFile.getSize())
			.type(fileType)
			.status(FileStatus.APPROVED)
			.url(s3Service.uploadFile(multipartFile))
			.isLatest(true)
			.writerId(1L)
			.workspaceId(workspaceId)
			.folderId(folderId)
			.rootFileId(1L)
			.parentFileId(null)
			.build();

		fileRepository.save(newFile);
	}

	private File getFile(Long fileId) {
		return fileRepository.findById(fileId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "File not found"));
	}

	private String getExtension(String fileName) {
		log.info("fileName: {}", fileName);
		return fileName.substring(fileName.lastIndexOf("."));
	}
}
