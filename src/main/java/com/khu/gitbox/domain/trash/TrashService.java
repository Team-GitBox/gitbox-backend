package com.khu.gitbox.domain.trash;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.file.entity.File;
import com.khu.gitbox.domain.file.infrastructure.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrashService {

    private final FileRepository fileRepository;

    public List<TrashFileResponse> showTrashFiles(Long workspaceId) {
        List<TrashFileResponse> trashFileResponses = new ArrayList<>();
        fileRepository.findAllByWorkspaceIdTrash(workspaceId)
                .forEach(file -> {
                    trashFileResponses.add(TrashFileResponse.of(file));
                });

        return trashFileResponses;
    }

    public void deleteFile(Long fileId) {
        fileRepository.deleteById(fileId);
    }

    @Transactional
    public void deleteFiles(List<Long> request) {
        request.forEach(id -> {
            fileRepository.deleteById(id);
        });
    }

    public void restoreFile(Long fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."));
        file.restore();
    }

    @Transactional
    public void restoreFiles(List<Long> request) {
        request.forEach(id -> {
            File file = fileRepository.findById(id)
                    .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."));
            file.restore();
        });
    }
}