package com.khu.gitbox.domain.file.application;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.file.entity.File;
import com.khu.gitbox.domain.file.entity.FileStatus;
import com.khu.gitbox.domain.file.entity.FileType;
import com.khu.gitbox.domain.file.infrastructure.FileRepository;
import com.khu.gitbox.domain.file.presentation.dto.NewVersionFileUploadRequest;
import com.khu.gitbox.s3.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FileService {
	private final FileRepository fileRepository;
	private final S3Service s3Service;

	// 파일 업로드
	public Long uploadFile(Long workspaceId, Long folderId, MultipartFile multipartFile) {
		final String fileName = multipartFile.getOriginalFilename();
		final FileType fileType = FileType.from(getExtension(fileName));

		final File file = File.builder()
			.name(fileName)
			.size(multipartFile.getSize())
			.type(fileType)
			.status(FileStatus.APPROVED)
			.url(s3Service.uploadFile(multipartFile))
			.isLatest(true)
			.writerId(1L) // TODO: 임시
			.workspaceId(workspaceId)
			.folderId(folderId)
			.rootFileId(null)
			.parentFileId(null)
			.build();
		return fileRepository.save(file).getId();
	}

	// 새로운 버전 파일 업로드 (+ PR 생성)
	public Long uploadNewVersionFile(Long parentFileId, NewVersionFileUploadRequest request,
		MultipartFile multipartFile) {
		// 부모 파일 정보 가져오기
		final File parentFile = getFile(parentFileId);

		// 부모 파일이 최신 버전인지 확인
		if (!parentFile.isLatest()) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "부모 파일이 최신 버전이 아닙니다.");
		}

		// 이미 업데이트 대기 중인 파일이 없는지 확인 (PR 여부)
		fileRepository.findPendingFile(parentFileId)
			.ifPresent(pendingFile -> {
				throw new CustomException(HttpStatus.BAD_REQUEST, "이미 업데이트 대기 중인 파일이 있습니다.");
			});

		// 파일 업로드 (PR 승인 시 부모 파일을 구버전으로)
		final String fileName = multipartFile.getOriginalFilename();
		final FileType fileType = FileType.from(getExtension(fileName));

		final File newVersionFile = File.builder()
			.name(fileName)
			.size(multipartFile.getSize())
			.type(fileType)
			.status(FileStatus.PENDING)
			.url(s3Service.uploadFile(multipartFile))
			.isLatest(false) // PR 승인 시 true로 변경되어야 함
			.writerId(1L)
			.workspaceId(parentFile.getWorkspaceId())
			.folderId(parentFile.getFolderId())
			.rootFileId(parentFile.getRootFileId())
			.parentFileId(parentFile.getId())
			.build();
		final File savedFile = fileRepository.save(newVersionFile);

		// TODO: PR 생성

		return savedFile.getId();
	}

	// 파일 버전 업데이트

	// 파일 이동

	// 파일 삭제

	private File getFile(Long fileId) {
		return fileRepository.findById(fileId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."));
	}

	private String getExtension(String fileName) {
		log.info("fileName: {}", fileName);
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

}
