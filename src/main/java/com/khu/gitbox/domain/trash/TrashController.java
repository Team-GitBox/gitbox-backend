package com.khu.gitbox.domain.trash;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khu.gitbox.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspace/{workspaceId}/trash")
public class TrashController {

	private final TrashService trashService;

	@GetMapping("")
	@Operation(summary = "휴지통 파일 목록 조회")
	public ApiResponse<List<TrashFileResponse>> showTrashFiles(
		@PathVariable("workspaceId") Long workspaceId) {
		List<TrashFileResponse> fileList = trashService.showTrashFiles(workspaceId);
		return ApiResponse.ok(fileList);
	}

	@DeleteMapping("/{fileId}")
	@Operation(summary = "휴지통 단일 파일 삭제하기")
	public ApiResponse<Void> deleteFile(@PathVariable("fileId") Long fileId) {
		trashService.deleteFile(fileId);
		return ApiResponse.ok();
	}

	@DeleteMapping("")
	@Operation(summary = "휴지통 여러 파일 삭제하기")
	public ApiResponse<Void> deleteFiles(@Valid @RequestBody List<Long> request) {
		trashService.deleteFiles(request);
		return ApiResponse.ok();
	}

	@PostMapping("/{fileId}")
	@Operation(summary = "휴지통 단일 파일 복구하기")
	public ApiResponse<Void> restoreFile(@PathVariable("fileId") Long fileId) {
		trashService.restoreFile(fileId);
		return ApiResponse.ok();
	}

	@PostMapping("")
	@Operation(summary = "휴지통 여러 파일 복구하기")
	public ApiResponse<Void> restoreFiles(@Valid @RequestBody List<Long> request) {
		trashService.restoreFiles(request);
		return ApiResponse.ok();
	}
}
