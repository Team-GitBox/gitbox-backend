package com.khu.gitbox.domain.action;

import com.khu.gitbox.domain.action.entity.Action;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionHistoryDto {
    private String name;
    private String fileName;
    private Action action;

}
