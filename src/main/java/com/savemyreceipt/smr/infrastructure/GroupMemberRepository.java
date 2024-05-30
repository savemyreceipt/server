package com.savemyreceipt.smr.infrastructure;

import com.savemyreceipt.smr.domain.Group;
import com.savemyreceipt.smr.domain.GroupMember;
import com.savemyreceipt.smr.exception.ErrorStatus;
import com.savemyreceipt.smr.exception.model.CustomException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    Long countByMemberId(Long memberId);

    Long countByGroupId(Long groupId);

    Optional<GroupMember> findByGroupIdAndMemberId(Long groupId, Long memberId);

    default GroupMember getGroupMemberByGroupIdAndMemberId(Long groupId, Long memberId) {
        return findByGroupIdAndMemberId(groupId, memberId).orElseThrow(
            () -> new CustomException(ErrorStatus.GROUP_MEMBER_NOT_FOUND, ErrorStatus.GROUP_MEMBER_NOT_FOUND.getMessage()));
    }

    List<GroupMember> findByMemberId(Long memberId);

    @Query("select gm.group from GroupMember gm where gm.member.id = :memberId")
    List<Group> findGroupsByMemberId(Long memberId);
}
