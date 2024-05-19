package com.khu.gitbox.domain.file.application;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.file.entity.Folder;
import com.khu.gitbox.domain.file.infrastructure.FileRepository;
import com.khu.gitbox.domain.file.infrastructure.FolderRepository;
import com.khu.gitbox.domain.file.presentation.dto.request.FolderCreateRequest;
import com.khu.gitbox.domain.file.presentation.dto.request.FolderUpdateRequest;
import com.khu.gitbox.domain.file.presentation.dto.response.FolderGetResponse;
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
		validateParentFolder(workspaceId, request.parentFolderId());

		final Folder newFolder = Folder.builder()
			.name(request.name())
			.parentFolderId(request.parentFolderId())
			.workspaceId(workspaceId)
			.build();

		return folderRepository.save(newFolder).getId();
	}

	@Transactional(readOnly = true)
	public FolderGetResponse getFolder(Long workspaceId, Long folderId) {
		final Folder folder = getAvailableFolder(workspaceId, folderId);
		return FolderGetResponse.of(folder);
	}

	public FolderGetResponse updateFolder(Long workspaceId, Long folderId, FolderUpdateRequest request) {
		final Folder folder = getAvailableFolder(workspaceId, folderId);
		folder.updateFolder(request.name(), request.parentFolderId());
		return FolderGetResponse.of(folder);
	}

	public void deleteFolder(Long workspaceId, Long folderId) {
		final Folder folder = getAvailableFolder(workspaceId, folderId);
		fileRepository.deleteByFolderId(folderId);
		folderRepository.delete(folder);
	}

	private Folder getAvailableFolder(Long workspaceId, Long folderId) {
		return folderRepository.findByIdAndWorkspaceId(folderId, workspaceId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "폴더를 찾을 수 없습니다."));
	}

	private void validateWorkspace(Long workspaceId) {
		workspaceRepository.findById(workspaceId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "워크스페이스를 찾을 수 없습니다."));
	}

	private void validateParentFolder(Long workspaceId, Long parentFolderId) {
		folderRepository.findByIdAndWorkspaceId(parentFolderId, workspaceId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "상위 폴더를 찾을 수 없습니다."));
	}
}
