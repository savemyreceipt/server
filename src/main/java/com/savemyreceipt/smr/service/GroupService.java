package com.savemyreceipt.smr.service;

import com.savemyreceipt.smr.DTO.group.request.GroupRequestDto;
import com.savemyreceipt.smr.DTO.group.response.GroupResponseDto;
import com.savemyreceipt.smr.domain.Group;
import com.savemyreceipt.smr.domain.GroupMember;
import com.savemyreceipt.smr.domain.Member;
import com.savemyreceipt.smr.enums.Role;
import com.savemyreceipt.smr.exception.ErrorStatus;
import com.savemyreceipt.smr.exception.model.CustomException;
import com.savemyreceipt.smr.infrastructure.GroupMemberRepository;
import com.savemyreceipt.smr.infrastructure.GroupRepository;
import com.savemyreceipt.smr.infrastructure.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Transactional(readOnly = true)
    public List<GroupResponseDto> getGroups(String email) {
        Member member = memberRepository.getMemberByEmail(email);
        List<GroupMember> groupMembers = groupMemberRepository.findByMemberId(member.getId());

        return groupMembers.stream()
            .map(groupMember -> GroupResponseDto.builder()
                .id(groupMember.getGroup().getId())
                .name(groupMember.getGroup().getName())
                .city(groupMember.getGroup().getCity())
                .organization(groupMember.getGroup().getOrganization())
                .description(groupMember.getGroup().getDescription())
                .memberCount(groupMemberRepository.countByGroupId(groupMember.getGroup().getId()))
                .isAccountant(groupMember.getRole().equals(Role.ACCOUNTANT))
                .build()).toList();
    }

    @Transactional(readOnly = true)
    public List<GroupResponseDto> searchGroup(String keyword) {
        List<Group> groups = groupRepository.findByNameContaining(keyword);
        return groups.stream()
            .map(group -> GroupResponseDto.builder()
                .id(group.getId())
                .name(group.getName())
                .city(group.getCity())
                .organization(group.getOrganization())
                .description(group.getDescription())
                .memberCount(groupMemberRepository.countByGroupId(group.getId()))
                .build()).toList();
    }

    @Transactional
    public void createGroup(String email, GroupRequestDto groupRequestDto) {
        Member member = memberRepository.getMemberByEmail(email);
        Group group = Group.builder()
            .name(groupRequestDto.getName())
            .city(groupRequestDto.getCity())
            .organization(groupRequestDto.getOrganization())
            .description(groupRequestDto.getDescription())
            .build();
        groupRepository.save(group);
        join(member, group, Role.ACCOUNTANT);
    }

    @Transactional
    public void joinGroup(String email, Long groupId, Role role) {
        Member member = memberRepository.getMemberByEmail(email);
        Group group = groupRepository.getGroupById(groupId);
        check(member);
        join(member, group, role);
    }

    private void check(Member member) {
        if (groupMemberRepository.countByMemberId(member.getId()) >= 10) {
            throw new CustomException(ErrorStatus.TOO_MUCH_GROUPS, ErrorStatus.TOO_MUCH_GROUPS.getMessage());
        }
    }

    private void join(Member member, Group group, Role role) {
        GroupMember groupMember = GroupMember.builder()
            .member(member)
            .group(group)
            .role(role)
            .build();
        groupMemberRepository.save(groupMember);
    }
}
