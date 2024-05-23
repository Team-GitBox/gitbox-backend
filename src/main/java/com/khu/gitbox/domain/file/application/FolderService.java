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
import com.khu.gitbox.domain.file.presentation.dto.response.FolderDetailGetResponse;
import com.khu.gitbox.domain.file.presentation.dto.response.FolderSummaryGetResponse;
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
		validateFolderCreation(workspaceId, request.parentFolderId(), request.name());

		final Folder newFolder = Folder.builder()
			.name(request.name())
			.parentFolderId(request.parentFolderId())
			.workspaceId(workspace.getId())
			.build();
		return folderRepository.save(newFolder).getId();
	}

	@Transactional(readOnly = true)
	public FolderDetailGetResponse getFolder(Long workspaceId, Long folderId) {
		final Folder folder = findFolderById(workspaceId, folderId);
		final List<FileGetResponse> files = getFilesInFolder(folderId);
		final List<FolderSummaryGetResponse> folders = getFoldersInFolder(folderId);
		return FolderDetailGetResponse.of(folder, folders, files);
	}

	public FolderDetailGetResponse updateFolder(Long workspaceId, Long folderId, FolderUpdateRequest request) {
		final Folder folder = findFolderById(workspaceId, folderId);
		folder.updateFolder(request.name(), request.parentFolderId());
		final List<FileGetResponse> files = getFilesInFolder(folderId);
		final List<FolderSummaryGetResponse> folders = getFoldersInFolder(folderId);
		return FolderDetailGetResponse.of(folder, folders, files);
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

	private void validateFolderCreation(Long workspaceId, Long parentFolderId, String folderName) {
		folderRepository.findByIdAndWorkspaceId(parentFolderId, workspaceId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "상위 폴더를 찾을 수 없습니다."));
		folderRepository.findByParentFolderIdAndName(parentFolderId, folderName)
			.ifPresent(folder -> {
				throw new CustomException(HttpStatus.BAD_REQUEST, "이미 존재하는 폴더 이름입니다 : " + folderName);
			});
	}

	private List<FileGetResponse> getFilesInFolder(Long folderId) {
		return fileRepository.findLatestByFolderId(folderId)
			.stream()
			.map(FileGetResponse::of)
			.toList();
	}

	private List<FolderSummaryGetResponse> getFoldersInFolder(Long folderId) {
		return folderRepository.findAllByParentFolderId(folderId)
			.stream()
			.map(FolderSummaryGetResponse::of)
			.toList();
	}
}
