package com.savemyreceipt.smr.service;

import com.savemyreceipt.smr.DTO.member.response.MemberResponseDto;
import com.savemyreceipt.smr.domain.Member;
import com.savemyreceipt.smr.enums.Role;
import com.savemyreceipt.smr.infrastructure.GroupMemberRepository;
import com.savemyreceipt.smr.infrastructure.GroupRepository;
import com.savemyreceipt.smr.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Transactional(readOnly = true)
    public MemberResponseDto getMember(String email) {
        Member member = memberRepository.getMemberByEmail(email);
        return MemberResponseDto.of(member);
    }
}
