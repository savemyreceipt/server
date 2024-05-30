package com.savemyreceipt.smr.infrastructure;

import com.savemyreceipt.smr.domain.Group;
import com.savemyreceipt.smr.domain.GroupMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    Long countByMemberId(Long memberId);

    Long countByGroupId(Long groupId);

    List<GroupMember> findByMemberId(Long memberId);

    @Query("select gm.group from GroupMember gm where gm.member.id = :memberId")
    List<Group> findGroupsByMemberId(Long memberId);
}
