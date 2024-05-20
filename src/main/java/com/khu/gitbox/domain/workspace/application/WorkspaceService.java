package com.khu.gitbox.domain.workspace.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.file.entity.Folder;
import com.khu.gitbox.domain.file.infrastructure.FileRepository;
import com.khu.gitbox.domain.file.infrastructure.FolderRepository;
import com.khu.gitbox.domain.member.application.MemberService;
import com.khu.gitbox.domain.member.entity.Member;
import com.khu.gitbox.domain.member.infrastructure.MemberRepository;
import com.khu.gitbox.domain.workspace.entity.Workspace;
import com.khu.gitbox.domain.workspace.entity.WorkspaceMember;
import com.khu.gitbox.domain.workspace.infrastructure.WorkspaceMemberRepository;
import com.khu.gitbox.domain.workspace.infrastructure.WorkspaceRepository;
import com.khu.gitbox.domain.workspace.presentation.dto.CreateWorkspace;
import com.khu.gitbox.domain.workspace.presentation.dto.MemberInfo;
import com.khu.gitbox.domain.workspace.presentation.dto.OwnerInfo;
import com.khu.gitbox.domain.workspace.presentation.dto.WorkspaceDetail;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkspaceService {
	private static final Long DEFAULT_MAX_STORAGE = 1000 * 1000 * 1000L; // 1GB
	private static final String DEFAULT_ROOT_FOLDER_NAME = "home"; // 루트 폴더 이름

	private final MemberRepository memberRepository;
	private final WorkspaceRepository workspaceRepository;
	private final WorkspaceMemberRepository workspaceMemberRepository;
	private final FolderRepository folderRepository;
	private final FileRepository fileRepository;
	private final MemberService memberService;

	public Long createWorkspace(CreateWorkspace request, Long ownerId) {
		Workspace workspace = Workspace.builder()
			.name(request.getName())
			.ownerId(ownerId)
			.maxStorage(DEFAULT_MAX_STORAGE)
			.build();
		Long workspaceId = workspaceRepository.save(workspace).getId();

		// 멤버 이메일 리스트를 순회하며 멤버 엔티티 생성 및 추가
		saveWorkspaceMembers(workspaceId, request.getMemberEmails());

		WorkspaceMember workspaceMember = WorkspaceMember.builder()
			.memberId(ownerId)
			.workspaceId(workspaceId)
			.build();
		workspaceMemberRepository.save(workspaceMember);

		Folder rootFolder = Folder.builder()
			.name(DEFAULT_ROOT_FOLDER_NAME)
			.workspaceId(workspaceId)
			.build();
		folderRepository.save(rootFolder);

		workspace.updateRootFolderId(rootFolder.getId());

		return workspace.getId();
	}

	public List<Long> addMembersToWorkspace(Long workspaceId, Long requestOwnerId, List<String> memberEmails) {
		Workspace workspace = findWorkspaceById(workspaceId);
		validateWorkspaceOwner(workspace, requestOwnerId);

		return saveWorkspaceMembers(workspaceId, memberEmails);
	}

	public void deleteWorkspace(Long workspaceId, Long requestOwnerId) {
		Workspace workspace = findWorkspaceById(workspaceId);
		validateWorkspaceOwner(workspace, requestOwnerId);

		workspaceRepository.deleteById(workspaceId);
		workspaceMemberRepository.deleteByWorkspaceId(workspaceId);
	}

	public void deleteMembers(Long workspaceId, Long requestOwnerId, List<String> memberEmails) {
		Workspace workspace = findWorkspaceById(workspaceId);
		validateWorkspaceOwner(workspace, requestOwnerId);

		List<Member> members = memberRepository.findByEmailIn(memberEmails);

		// 해당 멤버의 ID를 사용하여 workspaceMemberRepository에서 멤버 삭제
		members.stream().map(Member::getId).forEach(memberId -> {
			workspaceMemberRepository.deleteByWorkspaceIdAndMemberId(workspaceId, memberId);
		});
	}

	// 워크스페이스 정보
	public WorkspaceDetail getWorkspaceDetail(Long workspaceId, Long memberId) {
		// 워크스페이스 정보 가져오기
		Workspace workspace = findWorkspaceById(workspaceId);

		// 워크스페이스 멤버 여부 확인
		validateWorkspaceMember(workspace, memberId);

		// 워크스페이스로부터 소유자 정보 가져오기
		Member owner = memberService.findMemberById(workspace.getOwnerId());
		OwnerInfo ownerInfo = new OwnerInfo(owner.getEmail(), owner.getName());

		// 워크스페이스 멤버들의 이메일 목록 가져오기
		List<MemberInfo> memberInfoList = workspaceMemberRepository.findByWorkspaceId(workspaceId).stream()
			.map(workspaceMember -> {
				Member member = memberService.findMemberById(workspaceMember.getMemberId());
				return new MemberInfo(member.getEmail(), member.getName());
			}).toList();

		Map<String, Long> usedStorageByFileType = getUsedStorageByFileType(workspaceId);

		// WorkspaceDetail 객체 생성 및 반환
		return new WorkspaceDetail(
			workspace.getName(),
			workspace.getRootFolderId(),
			ownerInfo,
			memberInfoList,
			workspace.getMaxStorage(),
			workspace.getUsedStorage(),
			usedStorageByFileType);
	}

	public Workspace findWorkspaceById(Long workspaceId) {
		// 워크스페이스 id로 검색 로직 구현
		return workspaceRepository.findById(workspaceId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "워크스페이스를 찾을 수 없습니다."));
	}

	private void validateWorkspaceOwner(Workspace workspace, Long memberId) {
		if (!workspace.getOwnerId().equals(memberId)) {
			throw new CustomException(HttpStatus.FORBIDDEN, "해당 워크스페이스의 소유주가 아닙니다.");
		}
	}

	private void validateWorkspaceMember(Workspace workspace, Long memberId) {
		workspaceMemberRepository.findByWorkspaceIdAndMemberId(workspace.getId(), memberId)
			.orElseThrow(() -> new CustomException(HttpStatus.FORBIDDEN, "해당 워크스페이스의 멤버가 아닙니다."));
	}

	private List<Long> saveWorkspaceMembers(Long workspaceId, List<String> memberEmails) {
		List<Long> returnMemberIds = new ArrayList<>();
		for (String email : memberEmails) {
			// 이메일로 멤버 찾기
			Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "이메일을 잘못 입력하셨습니다."));

			// 이미 워크스페이스 멤버인지 확인
			if (workspaceMemberRepository.findByWorkspaceIdAndMemberId(workspaceId, member.getId()).isPresent()) {
				throw new CustomException(HttpStatus.BAD_REQUEST, "이미 워크스페이스 멤버입니다.");
			}

			// WorkspaceMember 객체 생성
			WorkspaceMember workspaceMember = WorkspaceMember.builder()
				.memberId(member.getId())
				.workspaceId(workspaceId)
				.build();
			// WorkspaceMember 저장
			workspaceMemberRepository.save(workspaceMember);

			// 멤버 ID 리스트에 추가
			returnMemberIds.add(member.getId());
		}
		return returnMemberIds;
	}

	private Map<String, Long> getUsedStorageByFileType(Long workspaceId) {
		Map<String, Long> usedStorage = new HashMap<>();
		fileRepository.findAllByWorkspaceId(workspaceId)
			.forEach(file -> {
				String fileType = file.getType().name();
				Long size = file.getSize();
				usedStorage.put(fileType, usedStorage.getOrDefault(fileType, 0L) + size);
			});
		return usedStorage;
	}
}
