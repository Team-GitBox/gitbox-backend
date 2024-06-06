package com.khu.gitbox.domain.trash;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.action.ActionHistoryService;
import com.khu.gitbox.domain.action.entity.Action;
import com.khu.gitbox.domain.file.entity.File;
import com.khu.gitbox.domain.file.infrastructure.FileRepository;
import com.khu.gitbox.domain.member.application.MemberService;
import com.khu.gitbox.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.khu.gitbox.util.SecurityContextUtil.getCurrentMemberId;

@Service
@RequiredArgsConstructor
public class TrashService {

    private final FileRepository fileRepository;
    private final MemberService memberService;
    private final ActionHistoryService actionHistoryService;

    public List<TrashFileResponse> showTrashFiles(Long workspaceId) {
        List<TrashFileResponse> trashFileResponses = new ArrayList<>();
        fileRepository.findAllByWorkspaceIdTrash(workspaceId)
                .forEach(file -> {
                    if (file.getRootFileId().equals(file.getId())) // root파일id와 pk값이 같으면 root파일.
                        trashFileResponses.add(TrashFileResponse.of(file));
                });

        return trashFileResponses;
    }

    @Transactional
    public void deleteFile(Long fileId, Long workspaceId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 파일입니다 : " + fileId));
        Member member = memberService.findMemberById(getCurrentMemberId());
        actionHistoryService.createActionHistory(workspaceId, member, file, Action.DELETE);

        fileRepository.deleteByRootFileId(fileId);

    }

    @Transactional
    public void restoreFile(Long fileId, Long workspaceId) {
        File f = fileRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 파일입니다 : " + fileId));
        Member member = memberService.findMemberById(getCurrentMemberId());
        actionHistoryService.createActionHistory(workspaceId, member, f, Action.RESTORE);

        fileRepository.findAllByRootFileIdTrash(fileId)
                .forEach(file -> {
                    file.restore();
                });
    }
}
