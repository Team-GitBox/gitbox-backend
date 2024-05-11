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
public class AddMembers {
    private Long workspaceId;
    private List<String> addMemberEmail;

}
