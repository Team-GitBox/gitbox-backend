package com.khu.gitbox.domain.trash;

import com.khu.gitbox.domain.file.infrastructure.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrashService {

    private final FileRepository fileRepository;

    public List<TrashFileResponse> showTrashFiles(Long workspaceId) {
        fileRepository
        return
    }

    public Long deleteFile() {
        return
    }

    public Long deleteFiles(List<String> request) {

    }

    public Long restoreFile() {

    }

    public Long restoreFiles(List<String> request) {

    }
}
