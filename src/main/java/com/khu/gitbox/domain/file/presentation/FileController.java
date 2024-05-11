package com.khu.gitbox.domain.file.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.khu.gitbox.common.response.ApiResponse;
import com.khu.gitbox.domain.file.application.FileService;
import com.khu.gitbox.domain.file.presentation.dto.FileGetResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {
	private final FileService fileService;

	@PostMapping(value = "/workspace/{workspaceId}/folders/{folderId}/files", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse<Long>> uploadFile(
		@PathVariable Long workspaceId,
		@PathVariable Long folderId,
		@RequestPart(value = "file") MultipartFile multipartFile) {
		final Long fileId = fileService.uploadFile(workspaceId, folderId, multipartFile);
		return ResponseEntity.ok(ApiResponse.ok(fileId));
	}

	@PostMapping(value = "/files/{parentFileId}", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse<Long>> uploadNewVersionFile(
		@PathVariable Long parentFileId,
		@RequestPart(value = "file") MultipartFile multipartFile) {
		final Long newFileId = fileService.uploadNewVersionFile(parentFileId, multipartFile);
		return ResponseEntity.ok(ApiResponse.ok(newFileId));
	}

	@GetMapping(value = "/files/{fileId}")
	public ResponseEntity<ApiResponse<FileGetResponse>> getFile(@PathVariable Long fileId) {
		final FileGetResponse response = fileService.getFileInfo(fileId);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@GetMapping(value = "/files/{fileId}/tree")
	public ResponseEntity<ApiResponse<List<FileGetResponse>>> getFileTree(@PathVariable Long fileId) {
		final List<FileGetResponse> fileTree = fileService.getFileTree(fileId);
		return ResponseEntity.ok(ApiResponse.ok(fileTree));
	}
}
