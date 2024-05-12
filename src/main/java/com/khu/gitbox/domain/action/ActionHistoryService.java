package com.khu.gitbox.domain.action;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.action.entity.ActionHistory;
import com.khu.gitbox.domain.action.infrastructure.ActionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.khu.gitbox.domain.action.entity.ActionHistory.toDto;

@Service
@RequiredArgsConstructor
public class ActionHistoryService {

    private final ActionHistoryRepository actionHistoryRepository;

    public Page<ActionHistoryDto> getActionHistoryList(int page, Pageable pageable, Long workspaceId) {

        int size = pageable.getPageSize();
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<ActionHistory> allByWorkspaceId
                = actionHistoryRepository.findAllByWorkspaceIdByOrderByIdDesc(workspaceId, pageRequest);
        Long total = actionHistoryRepository.countByWorkspaceId(workspaceId); // workspaceId가 같은것들 총 합

        if (total > (page * size)) throw new CustomException(HttpStatus.BAD_REQUEST, "현재 요청 방식이 잘못되었습니다.");

        if (allByWorkspaceId.isEmpty())
            throw new CustomException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다. 현재 워크스페이스에 히스토리를 찾을 수 없습니다.");

        allByWorkspaceId.stream().findFirst().orElseThrow(
                () -> {
                    throw new CustomException(HttpStatus.NO_CONTENT, "현재 워크스페이스에서 아무런 동작이 감지되지 랂습니다.");
                });

        long totalElements = allByWorkspaceId.getTotalElements();
        long requestCount = (allByWorkspaceId.getTotalPages() - 1) * allByWorkspaceId.getSize();

        if (!(totalElements > requestCount)) throw new CustomException(HttpStatus.BAD_REQUEST, "현재 요청 방식이 잘못되었습니다.");

        List<ActionHistory> entities = allByWorkspaceId.getContent();
        List<ActionHistoryDto> dtos = entities.stream().map(entity -> toDto(entity)).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, allByWorkspaceId.getTotalElements());
    }
}
