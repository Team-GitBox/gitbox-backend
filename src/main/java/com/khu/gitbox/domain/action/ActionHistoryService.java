package com.khu.gitbox.domain.action;

import java.util.List;

import org.springframework.stereotype.Service;

import com.khu.gitbox.domain.action.entity.Action;
import com.khu.gitbox.domain.action.entity.ActionHistory;
import com.khu.gitbox.domain.action.infrastructure.ActionHistoryRepository;
import com.khu.gitbox.domain.file.entity.File;
import com.khu.gitbox.domain.member.entity.Member;
import com.khu.gitbox.domain.workspace.presentation.dto.ActionHistoryGetResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActionHistoryService {
	private final ActionHistoryRepository actionHistoryRepository;

	public List<ActionHistoryGetResponse> getActionHistories(Long workspaceId) {
		return actionHistoryRepository.findAllByWorkspaceId(workspaceId).stream()
			.map(ActionHistoryGetResponse::of).toList();
	}

	public ActionHistory createActionHistory(Long workspaceId, Member actor, File targetFile, Action action) {
		final ActionHistory actionHistory = new ActionHistory(
			actor.getId(),
			actor.getName(),
			targetFile.getId(),
			targetFile.getName(),
			workspaceId,
			action
		);
		return actionHistoryRepository.save(actionHistory);
	}
	// public Page<ActionHistoryDto> getActionHistoryList(int page, Pageable pageable, Long workspaceId) {
	//
	// 	int size = pageable.getPageSize();
	// 	PageRequest pageRequest = PageRequest.of(page, size);
	//
	// 	Page<ActionHistory> allByWorkspaceId
	// 		= actionHistoryRepository.findAllByWorkspaceId(workspaceId, pageRequest);
	// 	Long total = actionHistoryRepository.countByWorkspaceId(workspaceId); // workspaceId가 같은것들 총 합
	//
	// 	if (total > (page * size))
	// 		throw new CustomException(HttpStatus.BAD_REQUEST, "현재 요청 방식이 잘못되었습니다.");
	//
	// 	if (allByWorkspaceId.isEmpty())
	// 		throw new CustomException(HttpStatus.NOT_FOUND, "현재 워크스페이스에 히스토리가 존재하지 않습니다.");
	//
	// 	long totalElements = allByWorkspaceId.getTotalElements();
	// 	long requestCount = (allByWorkspaceId.getTotalPages() - 1) * allByWorkspaceId.getSize();
	//
	// 	if (!(totalElements > requestCount))
	// 		throw new CustomException(HttpStatus.BAD_REQUEST, "현재 요청 방식이 잘못되었습니다.");
	//
	// 	List<ActionHistory> entities = allByWorkspaceId.getContent();
	// 	List<ActionHistoryDto> dtos = entities.stream().map(entity -> toDto(entity)).collect(Collectors.toList());
	//
	// 	return new PageImpl<>(dtos, pageable, allByWorkspaceId.getTotalElements());
	// }
}
