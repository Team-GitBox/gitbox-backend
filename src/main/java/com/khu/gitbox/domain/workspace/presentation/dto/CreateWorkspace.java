package com.khu.gitbox.domain.workspace.presentation.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateWorkspace {
	private String name;
	private List<String> memberEmails; // 추가할 멤버의 이메일 리스트
}
