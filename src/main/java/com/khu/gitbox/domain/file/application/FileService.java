package com.khu.gitbox.domain.file.application;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.file.entity.File;
import com.khu.gitbox.domain.file.entity.FileStatus;
import com.khu.gitbox.domain.file.entity.FileType;
import com.khu.gitbox.domain.file.infrastructure.FileRepository;
import com.khu.gitbox.domain.file.presentation.dto.FileCreateRequest;
import com.khu.gitbox.domain.file.presentation.dto.PullRequestCreateRequest;
import com.khu.gitbox.domain.file.presentation.dto.request.FileUpdateRequest;
import com.khu.gitbox.domain.file.presentation.dto.response.FileGetResponse;
import com.khu.gitbox.domain.member.entity.Member;
import com.khu.gitbox.domain.member.infrastructure.MemberRepository;
import com.khu.gitbox.domain.pullRequest.entity.PullRequest;
import com.khu.gitbox.domain.pullRequest.infrastructure.PullRequestRepository;
import com.khu.gitbox.domain.workspace.entity.Workspace;
import com.khu.gitbox.domain.workspace.infrastructure.WorkspaceRepository;
import com.khu.gitbox.s3.S3Service;
import com.khu.gitbox.util.SecurityContextUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FileService {
	private final FileRepository fileRepository;
	private final MemberRepository memberRepository;
	private final WorkspaceRepository workspaceRepository;
	private final S3Service s3Service;
	private final PullRequestRepository pullRequestRepository;

	// 파일 업로드
	public Long uploadFile(FileCreateRequest request, MultipartFile multipartFile) {
		final Member member = getCurrentMember();
		final Workspace workspace = getAvailableWorkspace(request.workspaceId());

		final String fileName = multipartFile.getOriginalFilename();
		final FileType fileType = FileType.from(getExtension(fileName));

		final File file = File.builder()
			.name(fileName)
			.size(multipartFile.getSize())
			.type(fileType)
			.status(FileStatus.APPROVED)
			.url(s3Service.uploadFile(multipartFile))
			.version(1L)
			.isLatest(true)
			.writerId(member.getId())
			.workspaceId(workspace.getId())
			.folderId(request.folderId())
			.rootFileId(null)
			.parentFileId(null)
			.build();

		workspace.increaseUsedStorage(file.getSize());

		final File savedFile = fileRepository.save(file);
		savedFile.updateRootFileId(savedFile.getId());
		return fileRepository.save(savedFile).getId();
	}

	// 새로운 버전 파일 업로드 (+ PR 생성)
	public Long uploadNewVersionFile(
		Long parentFileId,
		PullRequestCreateRequest request,
		MultipartFile multipartFile) {
		// 부모 파일 정보 가져오기
		final Member member = getCurrentMember();
		final File parentFile = getAvailableFile(parentFileId);

		// 부모 파일이 최신 버전인지 확인
		if (!parentFile.isLatest()) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "부모 파일이 최신 버전이 아닙니다.");
		}

		// 이미 업데이트 대기 중인 파일이 없는지 확인 (PR 여부)
		fileRepository.findPendingFile(parentFileId)
			.ifPresent(pendingFile -> {
				throw new CustomException(HttpStatus.BAD_REQUEST, "이미 업데이트 대기 중인 파일이 있습니다.");
			});

		// 파일 업로드 (PR 승인 시 부모 파일을 구버전으로)
		final String fileName = multipartFile.getOriginalFilename();
		final FileType fileType = FileType.from(getExtension(fileName));

		final File newVersionFile = File.builder()
			.name(fileName)
			.size(multipartFile.getSize())
			.type(fileType)
			.status(FileStatus.PENDING)
			.url(s3Service.uploadFile(multipartFile))
			.version(parentFile.getVersion() + 1)
			.isLatest(false) // PR 승인 시 true로 변경되어야 함
			.writerId(1L)
			.workspaceId(parentFile.getWorkspaceId())
			.folderId(parentFile.getFolderId())
			.rootFileId(parentFile.getRootFileId())
			.parentFileId(parentFile.getId())
			.build();
		final File savedFile = fileRepository.save(newVersionFile);

		// PR 생성
		final PullRequest pullRequest = PullRequest.builder()
			.title(request.title())
			.message(request.message())
			.writerId(member.getId())
			.fileId(savedFile.getId())
			.build();
		pullRequestRepository.save(pullRequest);

		// 워크스페이스 용량 업데이트
		final Workspace workspace = getAvailableWorkspace(parentFile.getWorkspaceId());
		workspace.increaseUsedStorage(savedFile.getSize());

		return savedFile.getId();
	}

	// 파일 조회
	public FileGetResponse getFileInfo(Long fileId) {
		return FileGetResponse.of(getAvailableFile(fileId));
	}

	// 파일 트리 조회
	public List<FileGetResponse> getFileTree(Long fileId) {
		final File file = getAvailableFile(fileId);
		return fileRepository.findAllByRootFileId(file.getRootFileId())
			.stream()
			.map(FileGetResponse::of).toList();
	}

	// 파일 업데이트 (이름 수정)
	public void updateFile(Long fileId, FileUpdateRequest request) {
		final File file = getAvailableFile(fileId);
		file.updateFileName(request.name());
	}

	// 파일 삭제
	public void deleteFile(Long fileId) {
		final File file = getAvailableFile(fileId);
		file.delete();
	}

	// 파일 트리 삭제
	public void deleteFileTree(Long fileId) {
		final File file = getAvailableFile(fileId);
		fileRepository.findAllByRootFileId(file.getRootFileId())
			.forEach(File::delete);
	}

	private Member getCurrentMember() {
		final Long memberId = SecurityContextUtil.getCurrentMemberId();
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
	}

	private File getAvailableFile(Long fileId) {
		return fileRepository.findById(fileId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."));
	}

	private Workspace getAvailableWorkspace(Long workspaceId) {
		return workspaceRepository.findById(workspaceId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "워크스페이스를 찾을 수 없습니다."));
	}

	private String getExtension(String fileName) {
		log.info("fileName: {}", fileName);
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

}
