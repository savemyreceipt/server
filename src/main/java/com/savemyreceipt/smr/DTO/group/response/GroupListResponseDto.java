package com.savemyreceipt.smr.DTO.group.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupListResponseDto {

    private Page<GroupResponseDto> groupList;

}
