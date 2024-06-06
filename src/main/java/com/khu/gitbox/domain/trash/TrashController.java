package com.khu.gitbox.domain.trash;

import com.khu.gitbox.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspace/{workspaceId}/trash")
public class TrashController {

    private final TrashService trashService;

    @GetMapping("")
    public ApiResponse<List<TrashFileResponse>> showTrashFiles(
            @PathVariable("workspaceId") Long workspaceId) {
        List<TrashFileResponse> fileList = trashService.showTrashFiles(workspaceId);
        return ApiResponse.ok(fileList);
    }

    @DeleteMapping("/{fileId}")
    @Operation(summary = "파일 삭제하기")
    public ApiResponse<Void> deleteFile(@PathVariable("fileId") Long fileId) {
        trashService.deleteFile(fileId);
        return ApiResponse.ok();
    }

    @PostMapping("/{fileId}")
    @Operation(summary = "파일 복구하기")
    public ApiResponse<Void> restoreFile(@PathVariable("fileId") Long fileId) {
        trashService.restoreFile(fileId);
        return ApiResponse.ok();
    }
}
