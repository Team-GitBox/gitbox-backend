package com.khu.gitbox.domain.workspace.application;

import com.khu.gitbox.common.exception.CustomException;
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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceService {
    private final MemberRepository memberRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository; // 멤버 리포지토리 주입

    public Long createWorkspace(CreateWorkspace request, Long ownerId) {
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
                throw new CustomException(HttpStatus.NOT_FOUND, email + " 이 메일은 회원이 아닙니다.");
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

    public void addMembers(List<String> memberEmails, Long workspaceId, Long memberId) {

        Workspace workspace = findById(workspaceId);

        // 워크스페이스의 ownerId와 현재 사용자의 ID 비교
        if (!workspace.getOwnerId().equals(memberId)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "해당 워크스페이스의 사용자가 아닙니다.");
        }

        // 보낸 이메일들이 멤버 디비에 있는지 확인
        for (String email : memberEmails) {
            // 이메일로 멤버 찾기
            Member member = memberRepository.findByEmail(email).orElseThrow(() -> {
                throw new CustomException(HttpStatus.NOT_FOUND, "이메일을 잘못 입력하셨습니다.");
            });

            // WorkspaceMember 객체 생성
            WorkspaceMember workspaceMember = WorkspaceMember.builder()
                    .memberId(member.getId())
                    .workspaceId(workspaceId)
                    .build();

            // WorkspaceMember 저장
            workspaceMemberRepository.save(workspaceMember);
        }
    }

    public Workspace findById(Long workspaceId) {
        // 워크스페이스 id로 검색 로직 구현
        return workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new EntityNotFoundException(workspaceId + "이 워크스페이스가 없습니다."));
    }

    @Transactional
    public void deleteWorkspaces(Long workspaceId, Long requestOwnerId) {
        if (!workspaceRepository.existsByOwnerId(requestOwnerId)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "해당 워크스페이스의 소유주가 아닙니다.");
        }

        workspaceRepository.deleteById(workspaceId);
        workspaceMemberRepository.deleteByWorkspaceId(workspaceId);

    }


    @Transactional
    public void deleteMembers(List<String> memberEmails, Long requestOwnerId) {
        Workspace workspace = workspaceRepository.findByOwnerId(requestOwnerId)
                .orElseThrow(() -> new CustomException(HttpStatus.FORBIDDEN, "해당 워크스페이스의 소유주가 아닙니다."));

        if (!workspace.getOwnerId().equals(requestOwnerId)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "해당 워크스페이스의 사용자가 아닙니다.");
        }
        List<Member> members = memberRepository.findByEmailIn(memberEmails);

        for (Member member : members) {
            // 해당 멤버의 ID를 사용하여 workspaceMemberRepository에서 멤버 삭제
            workspaceMemberRepository.deleteByMemberId(member.getId());
        }
    }

    // 워크스페이스 정보
    public WorkspaceDetail findByMemberIdAndWorkspaceId(Long workspaceId, Long memberId) {
        // 워크스페이스 멤버 확인
        WorkspaceMember workspaceMember = workspaceMemberRepository.findByMemberIdAndWorkspaceId(memberId, workspaceId)
                .orElseThrow(() -> new EntityNotFoundException("당신은 이 워크스페이스 사용자가 아닙니다."));

        // 워크스페이스 정보 가져오기
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new EntityNotFoundException("워크스페이스가 없습니다."));

        // 워크스페이스로부터 소유자 정보 가져오기
        OwnerInfo ownerInfo = memberRepository.findById(workspace.getOwnerId())
                .map(member -> new OwnerInfo(member.getEmail(), member.getName()))
                .orElseThrow(() -> new EntityNotFoundException("소유주가 없습니다."));

        // 워크스페이스 멤버들의 이메일 목록 가져오기
        List<MemberInfo> memberInfoList = workspaceMemberRepository.findByWorkspaceId(workspaceId).stream()
                .map(WorkspaceMember::getMemberId)
                .map(memberId2 -> memberRepository.findById(memberId)
                        .map(member -> new MemberInfo(member.getEmail(), member.getName()))
                        .orElse(new MemberInfo("", "뭔가 문제가 있습니다.")))
                .collect(Collectors.toList());

        // WorkspaceDetail 객체 생성 및 반환
        return new WorkspaceDetail(workspace.getName(), ownerInfo, memberInfoList,
                workspace.getMaxStorage(), workspace.getUsedStorage());
    }
}
