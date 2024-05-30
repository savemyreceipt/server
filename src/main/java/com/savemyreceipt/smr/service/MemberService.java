package com.savemyreceipt.smr.service;

import com.savemyreceipt.smr.DTO.member.request.MemberRequestDto;
import com.savemyreceipt.smr.DTO.member.response.MemberResponseDto;
import com.savemyreceipt.smr.domain.Member;
import com.savemyreceipt.smr.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResponseDto getMember(String email) {
        Member member = memberRepository.getMemberByEmail(email);
        return MemberResponseDto.of(member);
    }

    @Transactional
    public void updateMember(String email, MemberRequestDto memberRequestDto) {
        Member member = memberRepository.getMemberByEmail(email);

        member.updateName(memberRequestDto.getName());
        memberRepository.save(member);
    }
}
