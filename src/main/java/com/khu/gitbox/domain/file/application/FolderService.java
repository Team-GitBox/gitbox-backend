package com.khu.gitbox.domain.file.application;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.file.entity.Folder;
import com.khu.gitbox.domain.file.infrastructure.FileRepository;
import com.khu.gitbox.domain.file.infrastructure.FolderRepository;
import com.khu.gitbox.domain.file.presentation.dto.FolderCreateRequest;
import com.khu.gitbox.domain.file.presentation.dto.FolderGetResponse;
import com.khu.gitbox.domain.file.presentation.dto.FolderUpdateRequest;
import com.khu.gitbox.domain.workspace.infrastructure.WorkspaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FolderService {

	private final FolderRepository folderRepository;
	private final FileRepository fileRepository;
	private final WorkspaceRepository workspaceRepository;

	public Long createFolder(Long workspaceId, FolderCreateRequest request) {
		validateWorkspace(workspaceId);

		// TODO: 루트폴더를 워크스페이스 생성시 만들어야할 것 같음
		final Folder newFolder = Folder.builder()
			.name(request.name())
			.parentFolderId(request.parentFolderId())
			.workspaceId(workspaceId)
			.build();

		return folderRepository.save(newFolder).getId();
	}

	@Transactional(readOnly = true)
	public FolderGetResponse getFolder(Long workspaceId, Long folderId) {
		final Folder folder = getFolderEntity(workspaceId, folderId);

		return new FolderGetResponse(
			folder.getId(),
			folder.getName(),
			folder.getParentFolderId(),
			folder.getWorkspaceId()
		);
	}

	public FolderGetResponse updateFolder(Long workspaceId, Long folderId, FolderUpdateRequest request) {
		final Folder folder = getFolderEntity(workspaceId, folderId);

		folder.updateFolder(request.name(), request.parentFolderId());

		return new FolderGetResponse(
			folder.getId(),
			folder.getName(),
			folder.getParentFolderId(),
			folder.getWorkspaceId()
		);
	}

	public void deleteFolder(Long workspaceId, Long folderId) {
		final Folder folder = getFolderEntity(workspaceId, folderId);
		fileRepository.deleteByFolderId(folderId);
		folderRepository.delete(folder);
	}

	private void validateWorkspace(Long workspaceId) {
		workspaceRepository.findById(workspaceId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "워크스페이스를 찾을 수 없습니다."));
	}

	private Folder getFolderEntity(Long workspaceId, Long folderId) {
		return folderRepository.findByIdAndWorkspaceId(folderId, workspaceId)
			.orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다."));
	}

}