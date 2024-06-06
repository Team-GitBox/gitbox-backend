package com.khu.gitbox.domain.trash;

import com.khu.gitbox.domain.file.infrastructure.FileRepository;
import lombok.RequiredArgsConstructor;
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
                    if (file.getRootFileId().equals(file.getId())) // root파일id와 pk값이 같으면 root파일.
                        trashFileResponses.add(TrashFileResponse.of(file));
                });

        return trashFileResponses;
    }

    @Transactional
    public void deleteFile(Long fileId) {
        fileRepository.deleteByRootFileId(fileId);
    }

    @Transactional
    public void restoreFile(Long fileId) {
        fileRepository.findAllByRootFileIdTrash(fileId)
                .forEach(file -> {
                    file.restore();
                });
    }
}
