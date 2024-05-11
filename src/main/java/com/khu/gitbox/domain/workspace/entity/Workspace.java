package com.khu.gitbox.domain.workspace.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "workspace")
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)//팀이름
    private String name;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "used_storage", nullable = false)
    private Long usedStorage;

    @Column(name = "max_storage", nullable = false)
    private Long maxStorage;

    @Builder
    public Workspace(String name, Long ownerId, Long maxStorage, Long usedStorage) {
        this.name = name;
        this.ownerId = ownerId;
        this.maxStorage = maxStorage;
        this.usedStorage = usedStorage;
    }
}
