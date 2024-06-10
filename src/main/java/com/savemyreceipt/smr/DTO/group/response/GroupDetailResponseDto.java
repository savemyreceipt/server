package com.savemyreceipt.smr.DTO.group.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupDetailResponseDto {

    private Long id;
    private String name;
    private String city;
    private String organization;
    private String description;
    private Long memberCount;
    private Long receiptCount;
    private String accountantName;
    private boolean isAccountant;
}
