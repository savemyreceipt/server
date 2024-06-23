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
    private Long totalExpenditure;
    private String accountantName;
    private boolean isAccountant;

    @Builder
    public GroupResponseDto(Long id, String name, String city, String organization, String description, Long memberCount, Long receiptCount, Long totalExpenditure, String accountantName, boolean isAccountant) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.organization = organization;
        this.description = description;
        this.memberCount = memberCount;
        this.receiptCount = receiptCount;
        this.totalExpenditure = totalExpenditure;
        this.accountantName = accountantName;
        this.isAccountant = isAccountant;
    }
}
