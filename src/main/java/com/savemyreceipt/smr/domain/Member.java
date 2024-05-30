package com.savemyreceipt.smr.domain;

import com.savemyreceipt.smr.enums.Authority;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String profileUri;

    private String oauth2Id;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @OneToMany(mappedBy = "member")
    private Set<GroupMember> groupMembers;

    @Builder
    public Member(String email, String name, String profileUri, String oauth2Id) {
        this.email = email;
        this.name = name;
        this.profileUri = profileUri;
        this.oauth2Id = oauth2Id;
        this.authority = Authority.ROLE_USER;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateRole(String role) {
        this.authority = Authority.valueOf(role);
    }

}
