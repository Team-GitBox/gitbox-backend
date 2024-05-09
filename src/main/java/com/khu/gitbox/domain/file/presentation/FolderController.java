package com.khu.gitbox.domain.file.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khu.gitbox.common.response.ApiResponse;
import com.khu.gitbox.domain.file.application.FolderService;
import com.khu.gitbox.domain.file.presentation.dto.FolderCreateRequest;
import com.khu.gitbox.domain.file.presentation.dto.FolderGetResponse;
import com.khu.gitbox.domain.file.presentation.dto.FolderUpdateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/workspace/{workspaceId}/folder")
@RequiredArgsConstructor
public class FolderController {

	private final FolderService folderService;

	@PostMapping
	public ResponseEntity<ApiResponse<Long>> createFolder(
		@PathVariable Long workspaceId,
		@RequestBody FolderCreateRequest request) {
		Long folderId = folderService.createFolder(workspaceId, request);
		return ResponseEntity.ok(ApiResponse.ok(folderId));
	}

	@GetMapping("/{folderId}")
	public ResponseEntity<ApiResponse<FolderGetResponse>> getFolder(
		@PathVariable Long workspaceId,
		@PathVariable Long folderId) {
		FolderGetResponse folder = folderService.getFolder(workspaceId, folderId);
		return ResponseEntity.ok(ApiResponse.ok(folder));
	}

	@PatchMapping("/{folderId}")
	public ResponseEntity<ApiResponse<FolderGetResponse>> updateFolder(
		@PathVariable Long workspaceId,
		@PathVariable Long folderId,
		@RequestBody FolderUpdateRequest request) {
		FolderGetResponse folder = folderService.updateFolder(workspaceId, folderId, request);
		return ResponseEntity.ok(ApiResponse.ok(folder));
	}

	@DeleteMapping("/{folderId}")
	public ResponseEntity<ApiResponse<Void>> deleteFolder(
		@PathVariable Long workspaceId,
		@PathVariable Long folderId) {
		folderService.deleteFolder(workspaceId, folderId);
		return ResponseEntity.ok(ApiResponse.ok(null));
	}
}
