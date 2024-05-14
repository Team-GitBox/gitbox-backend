package com.khu.gitbox.domain.workspace.entity;

import org.springframework.http.HttpStatus;

import com.khu.gitbox.common.exception.CustomException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

	//용량 증가
	public void increaseUsedStorage(Long size) {
		if (this.usedStorage + size > this.maxStorage) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "최대 용량을 초과하였습니다.");
		}
		this.usedStorage += size;
	}

	//용량 감소
	public void decreaseUsedStorage(Long size) {
		if (this.usedStorage - size < 0) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "용량은 0보다 작을 수 없습니다.");
		}
		this.usedStorage -= size;
	}
}
