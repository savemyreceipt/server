package com.savemyreceipt.smr.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`group`")
@Getter
@NoArgsConstructor
public class Group extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String city;

    private String organization;

    private String description;

    @Builder
    public Group(String name, String city, String organization, String description) {
        this.name = name;
        this.city = city;
        this.organization = organization;
        this.description = description;
    }

    public Long getMemberCount() {
        return this.getGroupMembers().stream().count();
    }

}
