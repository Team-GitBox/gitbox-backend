package com.khu.gitbox.domain.file.presentation;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.khu.gitbox.domain.file.presentation.dto.request.FileGetByKeywordRequest;
import com.khu.gitbox.domain.file.presentation.dto.request.FileGetByTagRequest;
import com.khu.gitbox.domain.file.presentation.dto.request.FileUpdateRequest;
import com.khu.gitbox.domain.file.presentation.dto.request.PullRequestCreateRequest;
import com.khu.gitbox.domain.file.presentation.dto.response.FileGetResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
	private final FileService fileService;

	@Operation(summary = "새 파일 업로드")
	@PostMapping(value = "", consumes = "multipart/form-data")
	public ApiResponse<FileGetResponse> uploadFile(
		@RequestPart(value = "request") FileCreateRequest request,
		@RequestPart(value = "file") MultipartFile multipartFile) {
		final FileGetResponse response = fileService.uploadFile(request, multipartFile);
		return ApiResponse.ok(response);
	}

	@Operation(summary = "키워드로 파일 목록 조회")
	@GetMapping(value = "/keyword")
	public ApiResponse<List<FileGetResponse>> getFilesByKeyword(
		@Valid @ModelAttribute FileGetByKeywordRequest request) {
		final List<FileGetResponse> response = fileService.getFilesByKeyword(request);
		return ApiResponse.ok(response);
	}

	@Operation(summary = "태그로 파일 목록 조회")
	@GetMapping(value = "/tag")
	public ApiResponse<List<FileGetResponse>> getFilesByTag(@Valid @ModelAttribute FileGetByTagRequest request) {
		final List<FileGetResponse> response = fileService.getFilesByTag(request);
		return ApiResponse.ok(response);
	}

	@Operation(summary = "새 버전 파일 업로드 (풀 리퀘스트 생성)")
	@PostMapping(value = "/{parentFileId}", consumes = "multipart/form-data")
	public ApiResponse<FileGetResponse> uploadNewVersionFile(
		@PathVariable Long parentFileId,
		@RequestPart(value = "request") PullRequestCreateRequest request,
		@RequestPart(value = "file") MultipartFile multipartFile) {
		final FileGetResponse response = fileService.uploadNewVersionFile(parentFileId, request, multipartFile);
		return ApiResponse.ok(response);
	}

	@Operation(summary = "파일 정보 조회")
	@GetMapping(value = "/{fileId}")
	public ApiResponse<FileGetResponse> getFile(@PathVariable Long fileId) {
		final FileGetResponse response = fileService.getFileInfo(fileId);
		return ApiResponse.ok(response);
	}

	@Operation(summary = "파일 트리 조회")
	@GetMapping(value = "/{fileId}/tree")
	public ApiResponse<List<FileGetResponse>> getFileTree(@PathVariable Long fileId) {
		final List<FileGetResponse> fileTree = fileService.getFileTree(fileId);
		return ApiResponse.ok(fileTree);
	}

	@Operation(summary = "파일 정보 수정")
	@PatchMapping(value = "/{fileId}")
	public ApiResponse<Void> getFileTree(
		@PathVariable Long fileId,
		@Valid @RequestBody FileUpdateRequest request) {
		fileService.updateFile(fileId, request);
		return ApiResponse.ok();
	}

	@Operation(summary = "파일 삭제")
	@DeleteMapping(value = "/{fileId}")
	public ApiResponse<Void> deleteFile(@PathVariable Long fileId) {
		fileService.deleteFile(fileId);
		return ApiResponse.ok();
	}

	@Operation(summary = "파일 트리 삭제")
	@DeleteMapping(value = "/{fileId}/tree")
	public ApiResponse<Void> deleteFileTree(@PathVariable Long fileId) {
		fileService.deleteFileTree(fileId);
		return ApiResponse.ok();
	}
}
