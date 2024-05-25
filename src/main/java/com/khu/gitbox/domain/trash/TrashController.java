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
    public ApiResponse<Long> deleteFile(@PathVariable("fileId") Long fileId) {
        Long id = trashService.deleteFile();
        return ApiResponse.ok(id);
    }

    @DeleteMapping("")
    @Operation(summary = "여러 파일 삭제하기")
    public ApiResponse<Long> deleteFiles(@Valid @RequestBody List<String> request) {
        Long id = trashService.deleteFiles(request);
        return ApiResponse.ok(id);
    }

    @PostMapping("/{fileId}")
    @Operation(summary = "단일 파일 복구하기")
    public ApiResponse<Long> restoreFile(@PathVariable("fileId") Long fileId) {
        Long id = trashService.restoreFile();
        return ApiResponse.ok(id);
    }

    @PostMapping("")
    @Operation(summary = "여러 파일 복구하기")
    public ApiResponse<Long> restoreFiles(@Valid @RequestBody List<String> request) {
        Long id = trashService.restoreFiles(request);
        return ApiResponse.ok(id);
    }
}
