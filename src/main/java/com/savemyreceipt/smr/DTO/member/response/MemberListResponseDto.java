package com.savemyreceipt.smr.DTO.member.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberListResponseDto {

    private List<MemberResponseDto> memberList;
}
