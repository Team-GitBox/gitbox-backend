package com.khu.gitbox.domain.file.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.khu.gitbox.common.response.ApiResponse;
import com.khu.gitbox.domain.file.application.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {
	private final FileService fileService;

	@PostMapping(value = "/workspace/{workspaceId}/folders/{folderId}/files", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse> uploadFile(
		@PathVariable Long workspaceId,
		@PathVariable Long folderId,
		@RequestPart(value = "file") MultipartFile multipartFile) {
		fileService.uploadFile(workspaceId, folderId, multipartFile);
		return ResponseEntity.ok(ApiResponse.ok(null));
	}
}
