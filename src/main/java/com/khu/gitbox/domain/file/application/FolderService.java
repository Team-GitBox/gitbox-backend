package com.khu.gitbox.domain.file.application;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.file.entity.Folder;
import com.khu.gitbox.domain.file.infrastructure.FileRepository;
import com.khu.gitbox.domain.file.infrastructure.FolderRepository;
import com.khu.gitbox.domain.file.presentation.dto.request.FolderCreateRequest;
import com.khu.gitbox.domain.file.presentation.dto.request.FolderUpdateRequest;
import com.khu.gitbox.domain.file.presentation.dto.response.FileGetResponse;
import com.khu.gitbox.domain.file.presentation.dto.response.FolderGetResponse;
import com.khu.gitbox.domain.workspace.application.WorkspaceService;
import com.khu.gitbox.domain.workspace.entity.Workspace;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FolderService {
	private final FolderRepository folderRepository;
	private final FileRepository fileRepository;
	private final WorkspaceService workspaceService;

	public Long createFolder(Long workspaceId, FolderCreateRequest request) {
		final Workspace workspace = workspaceService.findWorkspaceById(workspaceId);
		validateParentFolder(workspaceId, request.parentFolderId());

		final Folder newFolder = Folder.builder()
			.name(request.name())
			.parentFolderId(request.parentFolderId())
			.workspaceId(workspace.getId())
			.build();
		return folderRepository.save(newFolder).getId();
	}

	@Transactional(readOnly = true)
	public FolderGetResponse getFolder(Long workspaceId, Long folderId) {
		final Folder folder = findFolderById(workspaceId, folderId);
		final List<FileGetResponse> files = getFilesInFolder(folderId);
		return FolderGetResponse.of(folder, files);
	}

	public FolderGetResponse updateFolder(Long workspaceId, Long folderId, FolderUpdateRequest request) {
		final Folder folder = findFolderById(workspaceId, folderId);
		folder.updateFolder(request.name(), request.parentFolderId());
		final List<FileGetResponse> files = getFilesInFolder(folderId);
		return FolderGetResponse.of(folder, files);
	}

	public void deleteFolder(Long workspaceId, Long folderId) {
		final Folder folder = findFolderById(workspaceId, folderId);
		fileRepository.deleteByFolderId(folderId);
		folderRepository.delete(folder);
	}

	public Folder findFolderById(Long workspaceId, Long folderId) {
		return folderRepository.findByIdAndWorkspaceId(folderId, workspaceId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "폴더를 찾을 수 없습니다."));
	}

	private void validateParentFolder(Long workspaceId, Long parentFolderId) {
		folderRepository.findByIdAndWorkspaceId(parentFolderId, workspaceId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "상위 폴더를 찾을 수 없습니다."));
	}

	private List<FileGetResponse> getFilesInFolder(Long folderId) {
		return fileRepository.findAllByFolderId(folderId)
			.stream()
			.map(FileGetResponse::of)
			.toList();
	}

}
