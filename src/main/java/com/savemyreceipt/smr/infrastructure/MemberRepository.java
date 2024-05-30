package com.savemyreceipt.smr.infrastructure;

import com.savemyreceipt.smr.domain.Member;
import com.savemyreceipt.smr.exception.ErrorStatus;
import com.savemyreceipt.smr.exception.model.CustomException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByOauth2Id(String oauth2Id);

    Optional<Member> findByEmail(String email);

    default Member getMemberByOauth2Id(String oauth2Id) {
        return findByOauth2Id(oauth2Id).orElseThrow(
            () -> new CustomException(ErrorStatus.MEMBER_NOT_FOUND, ErrorStatus.MEMBER_NOT_FOUND.getMessage())
        );
    }

    default Member getMemberByEmail(String email) {
        return findByEmail(email).orElseThrow(
            () -> new CustomException(ErrorStatus.MEMBER_NOT_FOUND, ErrorStatus.MEMBER_NOT_FOUND.getMessage())
        );
    }

}
