package com.savemyreceipt.smr.DTO.member.response;

import com.savemyreceipt.smr.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponseDto {

    private Long id;
    private String email;
    private String name;
    private String profileUri;

    @Builder
    public MemberResponseDto(Long id, String email, String name, String profileUri) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.profileUri = profileUri;
    }

    public static MemberResponseDto of(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .profileUri(member.getProfileUri())
                .build();
    }
}
