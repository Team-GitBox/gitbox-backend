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
public class CreateWorkspace {
    private String name;
    private Long maxStorage;
    private Long usedStorage;
    private List<String> memberEmails; // 추가할 멤버의 이메일 리스트
}
