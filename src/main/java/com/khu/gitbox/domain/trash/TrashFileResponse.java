package com.khu.gitbox.domain.trash;

import com.khu.gitbox.domain.file.entity.File;

public record TrashFileResponse(
        Long id,
        String name
) {

    public static TrashFileResponse of(File file) {
        return new TrashFileResponse(
                file.getId(),
                file.getName()
        );
    }

}
