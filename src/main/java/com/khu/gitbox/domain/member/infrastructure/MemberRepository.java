package com.khu.gitbox.domain.member.infrastructure;

import java.util.Optional;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.khu.gitbox.domain.member.entity.Member;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

//	@Modifying
//	@Transactional
//	Optional<Member> updateMemberById(@Param(value = "id") Long id, @Param(value = "M") Member member);


}

