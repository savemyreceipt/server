package com.savemyreceipt.smr.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Notification extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String message;

    private boolean checked = false;

    @ManyToOne
    private Member member;

    @Builder
    public Notification(String title, String message, Member member) {
        this.title = title;
        this.message = message;
        this.member = member;
    }

    public void check() {
        this.checked = true;
    }

}
