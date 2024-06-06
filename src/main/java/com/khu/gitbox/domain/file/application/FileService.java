package com.khu.gitbox.domain.file.application;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.action.ActionHistoryService;
import com.khu.gitbox.domain.action.entity.Action;
import com.khu.gitbox.domain.file.entity.File;
import com.khu.gitbox.domain.file.entity.FileStatus;
import com.khu.gitbox.domain.file.entity.FileType;
import com.khu.gitbox.domain.file.entity.Folder;
import com.khu.gitbox.domain.file.infrastructure.FileRepository;
import com.khu.gitbox.domain.file.presentation.dto.request.*;
import com.khu.gitbox.domain.file.presentation.dto.response.FileGetResponse;
import com.khu.gitbox.domain.member.application.MemberService;
import com.khu.gitbox.domain.member.entity.Member;
import com.khu.gitbox.domain.pullRequest.entity.PullRequest;
import com.khu.gitbox.domain.pullRequest.infrastructure.PullRequestRepository;
import com.khu.gitbox.domain.workspace.application.WorkspaceService;
import com.khu.gitbox.domain.workspace.entity.Workspace;
import com.khu.gitbox.domain.workspace.entity.WorkspaceMember;
import com.khu.gitbox.domain.workspace.infrastructure.WorkspaceMemberRepository;
import com.khu.gitbox.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.khu.gitbox.util.SecurityContextUtil.getCurrentMemberId;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final PullRequestRepository pullRequestRepository;
    private final MemberService memberService;
    private final WorkspaceService workspaceService;
    private final FolderService folderService;
    private final ActionHistoryService actionHistoryService;
    private final S3Service s3Service;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    // 파일 업로드
    public FileGetResponse uploadFile(FileCreateRequest request, MultipartFile multipartFile) {
        final Member member = memberService.findMemberById(getCurrentMemberId());
        final Workspace workspace = workspaceService.findWorkspaceById(request.workspaceId());
        final Folder folder = folderService.findFolderById(request.workspaceId(), request.folderId());
        final String fileName = multipartFile.getOriginalFilename();
        final FileType fileType = FileType.from(getExtension(fileName));
        validateFileName(folder.getId(), fileName);

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
                .folderId(folder.getId())
                .rootFileId(null)
                .parentFileId(null)
                .build();
        workspace.increaseUsedStorage(file.getSize());

        final File savedFile = fileRepository.save(file);
        savedFile.updateRootFileId(savedFile.getId());
        actionHistoryService.createActionHistory(workspace.getId(), member, savedFile, Action.UPLOAD);

        return FileGetResponse.of(fileRepository.save(savedFile));
    }


    // 새로운 버전 파일 업로드 & PR 생성
    public FileGetResponse uploadNewVersionFile(
            Long parentFileId,
            PullRequestCreateRequest request,
            MultipartFile multipartFile) {
        // 부모 파일 정보 가져오기
        final Member member = memberService.findMemberById(getCurrentMemberId());
        final File parentFile = findFileById(parentFileId);
        final List<WorkspaceMember> workspaceMembers = workspaceMemberRepository.findByWorkspaceId(
                parentFile.getWorkspaceId());
        // 부모 파일 업데이트 가능 여부 확인
        validateParentFileForUpdate(parentFileId, parentFile);

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

        if (workspaceMembers.size() > 1) {
            // PR 생성 (PR 승인 시 부모 파일을 구버전으로)
            createPullRequest(request, member, savedFile);
        } else {
            // PR 승인
            savedFile.approve(parentFile);
        }

        // 워크스페이스 용량 업데이트
        final Workspace workspace = workspaceService.findWorkspaceById(parentFile.getWorkspaceId());
        workspace.increaseUsedStorage(savedFile.getSize());

        actionHistoryService.createActionHistory(workspace.getId(), member, savedFile, Action.PULL_REQUEST);
        return FileGetResponse.of(savedFile);
    }

    private void createPullRequest(PullRequestCreateRequest request, Member member, File savedFile) {
        // PR 생성
        final PullRequest pullRequest = PullRequest.builder()
                .title(request.pullRequestTitle())
                .message(request.pullRequestMessage())
                .writerId(member.getId())
                .fileId(savedFile.getId())
                .build();
        pullRequestRepository.save(pullRequest);

        // 파일 PR ID 설정
        savedFile.updatePullRequestId(pullRequest.getId());
    }

    // 파일 조회
    public FileGetResponse getFileInfo(Long fileId) {
        return FileGetResponse.of(findFileById(fileId));
    }

    // 파일 트리 조회
    public List<FileGetResponse> getFileTree(Long fileId) {
        final File file = findFileById(fileId);
        return fileRepository.findAllByRootFileId(file.getRootFileId())
                .stream()
                .map(FileGetResponse::of).toList();
    }

    // 파일 업데이트 (이름 수정)
    public void updateFile(Long fileId, FileUpdateRequest request) {
        final File file = findFileById(fileId);
        file.updateFile(request.name(), request.tag());
    }

    // 파일 삭제
    public void deleteFile(Long fileId) {
        final File file = findFileById(fileId);
        final Member member = memberService.findMemberById(getCurrentMemberId());
        file.delete();
        actionHistoryService.createActionHistory(file.getWorkspaceId(), member, file, Action.TRASH);
    }

    // 파일 트리 삭제
    public void deleteFileTree(Long fileId) {
        final File targetFile = findFileById(fileId);
        final Member member = memberService.findMemberById(getCurrentMemberId());

        fileRepository.findAllByRootFileId(targetFile.getRootFileId())
                .forEach(file -> {
                    file.delete();
                    actionHistoryService.createActionHistory(file.getWorkspaceId(), member, file, Action.TRASH);
                });
    }

    public File findFileById(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."));
    }

    private String getExtension(String fileName) {
        log.info("fileName: {}", fileName);
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private void validateFileName(Long folderId, String fileName) {
        // 같은 이름의 파일이 이미 존재하는지 확인
        fileRepository.findByFolderIdAndName(folderId, fileName)
                .ifPresent(file -> {
                    throw new CustomException(HttpStatus.BAD_REQUEST, "같은 이름의 파일이 이미 존재합니다.");
                });
    }

    private void validateParentFileForUpdate(Long parentFileId, File parentFile) {
        // 부모 파일이 최신 버전인지 확인
        if (!parentFile.isLatest()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "부모 파일이 최신 버전이 아닙니다.");
        }

        // 이미 업데이트 대기 중인 파일이 없는지 확인 (PR 여부)
        fileRepository.findPendingFile(parentFileId)
                .ifPresent(pendingFile -> {
                    throw new CustomException(HttpStatus.BAD_REQUEST, "이미 업데이트 대기 중인 파일이 있습니다.");
                });
    }

    public List<FileGetResponse> getFilesByTag(FileGetByTagRequest request) {
        return fileRepository.findAllByTag(request.workspaceId(), request.tag())
                .stream()
                .map(FileGetResponse::of)
                .toList();
    }

    public List<FileGetResponse> getFilesByKeyword(FileGetByKeywordRequest request) {
        return fileRepository.findAllByKeyword(request.workspaceId(), request.keyword())
                .stream()
                .map(FileGetResponse::of)
                .toList();
    }
}
