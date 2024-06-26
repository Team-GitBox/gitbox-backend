package com.khu.gitbox.domain.workspace.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteMembers {
    private List<String> memberEmails; // 삭제할 멤버의 이메일 리스트
}
