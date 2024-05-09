package com.khu.gitbox.domain.file.application;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.file.entity.Folder;
import com.khu.gitbox.domain.file.infrastructure.FolderRepository;
import com.khu.gitbox.domain.file.presentation.dto.FolderCreateRequest;
import com.khu.gitbox.domain.file.presentation.dto.FolderGetResponse;
import com.khu.gitbox.domain.workspace.infrastructure.WorkspaceRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FolderService {

	private final FolderRepository folderRepository;
	private final WorkspaceRepository workspaceRepository;

	public Long createFolder(Long workspaceId, FolderCreateRequest request) {
		validateWorkspace(workspaceId);

		// 루트폴더를 워크스페이스 생성시 만들어야할 것 같다.
		final Folder newFolder = Folder.builder()
			.name(request.name())
			.parentFolderId(request.parentFolderId())
			.workspaceId(workspaceId)
			.build();

		return folderRepository.save(newFolder).getId();
	}

	public FolderGetResponse getFolder(Long workspaceId, Long folderId) {
		Folder folder = folderRepository.findByIdAndWorkspaceId(folderId, workspaceId)
			.orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다."));

		return new FolderGetResponse(
			folder.getId(),
			folder.getName(),
			folder.getParentFolderId(),
			folder.getWorkspaceId()
		);
	}

	private void validateWorkspace(Long workspaceId) {
		workspaceRepository.findById(workspaceId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "워크스페이스를 찾을 수 없습니다."));
	}
}
