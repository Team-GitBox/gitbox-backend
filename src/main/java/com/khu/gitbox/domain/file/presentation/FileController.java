package com.khu.gitbox.domain.file.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.khu.gitbox.common.response.ApiResponse;
import com.khu.gitbox.domain.file.application.FileService;
import com.khu.gitbox.domain.file.presentation.dto.request.FileCreateRequest;
import com.khu.gitbox.domain.file.presentation.dto.request.FileUpdateRequest;
import com.khu.gitbox.domain.file.presentation.dto.request.PullRequestCreateRequest;
import com.khu.gitbox.domain.file.presentation.dto.response.FileGetResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {
	private final FileService fileService;

	@PostMapping(value = "/files", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse<FileGetResponse>> uploadFile(
		@RequestPart(value = "request") FileCreateRequest request,
		@RequestPart(value = "file") MultipartFile multipartFile) {
		final FileGetResponse response = fileService.uploadFile(request, multipartFile);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@PostMapping(value = "/files/{parentFileId}", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse<FileGetResponse>> uploadNewVersionFile(
		@PathVariable Long parentFileId,
		@RequestPart(value = "request") PullRequestCreateRequest request,
		@RequestPart(value = "file") MultipartFile multipartFile) {
		final FileGetResponse response = fileService.uploadNewVersionFile(parentFileId, request, multipartFile);
		return ResponseEntity.ok(ApiResponse.ok(response));
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

	@PatchMapping(value = "/files/{fileId}")
	public ResponseEntity<ApiResponse<Void>> getFileTree(
		@PathVariable Long fileId,
		@RequestBody FileUpdateRequest request) {
		fileService.updateFile(fileId, request);
		return ResponseEntity.ok(ApiResponse.ok());
	}

	@DeleteMapping(value = "/files/{fileId}")
	public ResponseEntity<ApiResponse<Void>> deleteFile(@PathVariable Long fileId) {
		fileService.deleteFile(fileId);
		return ResponseEntity.ok(ApiResponse.ok());
	}

	@DeleteMapping(value = "/files/{fileId}/tree")
	public ResponseEntity<ApiResponse<Void>> deleteFileTree(@PathVariable Long fileId) {
		fileService.deleteFileTree(fileId);
		return ResponseEntity.ok(ApiResponse.ok());
	}
}
