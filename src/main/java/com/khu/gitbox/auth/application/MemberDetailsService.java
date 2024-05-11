package com.khu.gitbox.auth.application;

import com.khu.gitbox.domain.member.entity.Member;
import com.khu.gitbox.domain.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn(">>>>> Member not found : {}", email);
                    return new UsernameNotFoundException("MEMBER_NOT_FOUND");
                });


        List<GrantedAuthority> authorities = getAuthorities(member);

        return UserDetailsImpl.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .authorities(authorities)
                .build();
    }

    private List<GrantedAuthority> getAuthorities(Member member) {
        return member.getRole() != null ?
                List.of(new SimpleGrantedAuthority(member.getRole().name())) : null;
    }
}
