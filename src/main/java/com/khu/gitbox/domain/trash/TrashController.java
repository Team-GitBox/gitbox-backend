package com.khu.gitbox.domain.trash;

import com.khu.gitbox.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{workspaceId}/trash")
public class TrashController {

    private final TrashService trashService;

    @GetMapping("")
    public ApiResponse<List<TrashFileResponse>> showTrashFiles(
            @PathVariable("workspaceId") Long workspaceId) {
        List<TrashFileResponse> fileList = trashService.showTrashFiles(workspaceId);
        return ApiResponse.ok(fileList);
    }

    @DeleteMapping("/{fileId}")
    @Operation(summary = "단일 파일 삭제하기")
    public ApiResponse<Void> deleteFile(@PathVariable("fileId") Long fileId) {
        trashService.deleteFile(fileId);
        return ApiResponse.ok();
    }

    @DeleteMapping("")
    @Operation(summary = "여러 파일 삭제하기")
    public ApiResponse<Void> deleteFiles(@Valid @RequestBody List<String> request) {
        trashService.deleteFiles(request);
        return ApiResponse.ok();
    }

    @PostMapping("/{fileId}")
    @Operation(summary = "단일 파일 복구하기")
    public ApiResponse<Void> restoreFile(@PathVariable("fileId") Long fileId) {
        trashService.restoreFile(fileId);
        return ApiResponse.ok();
    }

    @PostMapping("")
    @Operation(summary = "여러 파일 복구하기")
    public ApiResponse<Void> restoreFiles(@Valid @RequestBody List<String> request) {
        trashService.restoreFiles(request);
        return ApiResponse.ok();
    }
}
