package com.savemyreceipt.smr.DTO.group.response;

import com.savemyreceipt.smr.domain.GroupMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupResponseDto {

    private Long id;
    private String name;
    private String city;
    private String organization;
    private String description;
    private Long memberCount;
    private Long receiptCount;
    private boolean isAccountant;

    @Builder
    public GroupResponseDto(Long id, String name, String city, String organization, String description, Long memberCount, Long receiptCount, boolean isAccountant) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.organization = organization;
        this.description = description;
        this.memberCount = memberCount;
        this.receiptCount = receiptCount;
        this.isAccountant = isAccountant;
    }
}
