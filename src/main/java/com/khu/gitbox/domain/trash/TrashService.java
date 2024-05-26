package com.khu.gitbox.domain.trash;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.file.entity.File;
import com.khu.gitbox.domain.file.infrastructure.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
        if (trashFileResponses.isEmpty())
            throw new CustomException(HttpStatus.NOT_FOUND, "휴지통이 비어있습니다.");
        return trashFileResponses;
    }

    public void deleteFile(Long fileId) {
        fileRepository.deleteById(fileId);
    }

    public void deleteFiles(List<String> request) {
        request.forEach(name -> {
            fileRepository.deleteByName(name);
        });
    }

    public void restoreFile(Long fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."));
        file.restore();
    }

    public void restoreFiles(List<String> request) {
        request.forEach(name -> {
            File file = fileRepository.findByName(name)
                    .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."));
            file.restore();
        });
    }
}
