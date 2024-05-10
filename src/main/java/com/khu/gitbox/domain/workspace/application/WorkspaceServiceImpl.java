package com.khu.gitbox.domain.workspace.application;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.member.entity.Member;
import com.khu.gitbox.domain.member.infrastructure.MemberRepository;
import com.khu.gitbox.domain.workspace.entity.Workspace;
import com.khu.gitbox.domain.workspace.entity.WorkspaceMember;
import com.khu.gitbox.domain.workspace.infrastructure.WorkspaceMemberRepository;
import com.khu.gitbox.domain.workspace.infrastructure.WorkspaceRepository;
import com.khu.gitbox.domain.workspace.presentation.dto.MakeWorkspace;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService{
	private final MemberRepository memberRepository;
	private final WorkspaceRepository workspaceRepository;
	private final WorkspaceMemberRepository workspaceMemberRepository; // 멤버 리포지토리 주입

	public Long save(MakeWorkspace request, Long ownerId) {
		Workspace workspace = Workspace.builder()
				.name(request.getName())
				.ownerId(ownerId)
				.maxStorage(request.getMaxStorage())
				.usedStorage(request.getUsedStorage())
				.build();

		Long id = workspaceRepository.save(workspace).getId();

		// 멤버 이메일 리스트를 순회하며 멤버 엔티티 생성 및 추가
		for (String email : request.getMemberEmails()) {

			Member member = memberRepository.findByEmail(email).orElseThrow(() -> {
				throw new CustomException(HttpStatus.NOT_FOUND, email + " 이 메일은 없습니당");
			});

			WorkspaceMember workspaceMember = WorkspaceMember.builder()
					.memberId(member.getId())
					.workspaceId(id)
					.build();

			workspaceMemberRepository.save(workspaceMember);
		}

		WorkspaceMember workspaceMember = WorkspaceMember.builder()
				.memberId(ownerId)
				.workspaceId(id)
				.build();
		workspaceMemberRepository.save(workspaceMember);

		return workspace.getId();
	}

	@Override
	public Workspace findById(Long workspaceId) {
		// 워크스페이스 id로 검색 로직 구현
		return workspaceRepository.findById(workspaceId).orElseThrow(() -> new EntityNotFoundException("Workspace not found with name: " + workspaceId));
	}

//	@Override
//	public WorkspaceMember findByEmail(String email) {
//		// 워크스페이스 id로 검색 로직 구현
//		return workspaceMemberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Workspace not found with name: " + email));
//	}

	@Override
	public void delete(Long id) {
		// 워크스페이스 삭제 로직 구현
		workspaceRepository.deleteById(id);
	}
}



