package com.khu.gitbox.domain.file.presentation;

import com.khu.gitbox.common.response.ApiResponse;
import com.khu.gitbox.domain.file.application.FolderService;
import com.khu.gitbox.domain.file.presentation.dto.request.FolderCreateRequest;
import com.khu.gitbox.domain.file.presentation.dto.request.FolderUpdateRequest;
import com.khu.gitbox.domain.file.presentation.dto.response.FolderDetailGetResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workspace/{workspaceId}/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    public ApiResponse<Long> createFolder(
            @PathVariable Long workspaceId,
            @Valid @RequestBody FolderCreateRequest request) {
        Long folderId = folderService.createFolder(workspaceId, request);
        return ApiResponse.ok(folderId);
    }

    @GetMapping("/{folderId}")
    public ApiResponse<FolderDetailGetResponse> getFolder(
            @PathVariable Long workspaceId,
            @PathVariable Long folderId) {
        FolderDetailGetResponse folder = folderService.getFolder(workspaceId, folderId);
        return ApiResponse.ok(folder);
    }

    @PatchMapping("/{folderId}")
    public ApiResponse<FolderDetailGetResponse> updateFolder(
            @PathVariable Long workspaceId,
            @PathVariable Long folderId,
            @Valid @RequestBody FolderUpdateRequest request) {
        FolderDetailGetResponse folder = folderService.updateFolder(workspaceId, folderId, request);
        return ApiResponse.ok(folder);
    }

    @DeleteMapping("/{folderId}")
    public ApiResponse<Void> deleteFolder(
            @PathVariable Long workspaceId,
            @PathVariable Long folderId) {
        folderService.deleteFolder(workspaceId, folderId);
        return ApiResponse.ok(null);
    }
}
