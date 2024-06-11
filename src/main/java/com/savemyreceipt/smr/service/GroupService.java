package com.savemyreceipt.smr.service;

import com.savemyreceipt.smr.DTO.group.request.GroupRequestDto;
import com.savemyreceipt.smr.DTO.group.response.GroupListResponseDto;
import com.savemyreceipt.smr.DTO.group.response.GroupResponseDto;
import com.savemyreceipt.smr.DTO.member.response.MemberListResponseDto;
import com.savemyreceipt.smr.DTO.member.response.MemberResponseDto;
import com.savemyreceipt.smr.DTO.receipt.response.ReceiptListResponseDto;
import com.savemyreceipt.smr.DTO.receipt.response.ReceiptResponseDto;
import com.savemyreceipt.smr.domain.Group;
import com.savemyreceipt.smr.domain.GroupMember;
import com.savemyreceipt.smr.domain.Member;
import com.savemyreceipt.smr.domain.Receipt;
import com.savemyreceipt.smr.enums.Role;
import com.savemyreceipt.smr.exception.ErrorStatus;
import com.savemyreceipt.smr.exception.model.CustomException;
import com.savemyreceipt.smr.infrastructure.GroupMemberRepository;
import com.savemyreceipt.smr.infrastructure.GroupRepository;
import com.savemyreceipt.smr.infrastructure.MemberRepository;
import com.savemyreceipt.smr.infrastructure.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ReceiptRepository receiptRepository;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public GroupListResponseDto getGroups(String email, int page) {
        Member member = memberRepository.getMemberByEmail(email);
        Pageable pageable = PageRequest.of(page, 12);

        Page<GroupMember> groupMembers = groupMemberRepository.findByMemberId(member.getId(), pageable);

        Page<GroupResponseDto> groupResponseDtos = groupMembers.map(
            groupMember -> GroupResponseDto.builder()
                .id(groupMember.getGroup().getId())
                .name(groupMember.getGroup().getName())
                .city(groupMember.getGroup().getCity())
                .organization(groupMember.getGroup().getOrganization())
                .description(groupMember.getGroup().getDescription())
                .memberCount(groupMemberRepository.countByGroupId(groupMember.getGroup().getId()))
                .receiptCount(receiptRepository.countByGroup(groupMember.getGroup()))
                .accountantName(findAccountant(groupMember.getGroup()).getName())
                .isAccountant(groupMember.getRole().equals(Role.ACCOUNTANT))
                .build()
        );
        return new GroupListResponseDto(groupResponseDtos);
    }

    @Transactional(readOnly = true)
    public GroupResponseDto getGroup(String email, Long groupId) {
        Member member = memberRepository.getMemberByEmail(email);
        Group group = groupRepository.getGroupById(groupId);
        GroupMember groupMember = groupMemberRepository.getGroupMemberByGroupIdAndMemberId(groupId, member.getId());
        return GroupResponseDto.builder()
            .id(group.getId())
            .name(group.getName())
            .city(group.getCity())
            .organization(group.getOrganization())
            .description(group.getDescription())
            .memberCount(groupMemberRepository.countByGroupId(group.getId()))
            .receiptCount(receiptRepository.countByGroup(group))
            .accountantName(findAccountant(group).getName())
            .isAccountant(groupMember.getRole().equals(Role.ACCOUNTANT))
            .build();
    }

    @Transactional(readOnly = true)
    public GroupListResponseDto searchGroup(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 12);
//        Page<Group> groups = groupRepository.findByNameContaining(keyword, pageable);
        Page<Group> groups = groupRepository.findByNameContainingOrderByMemberCountDesc(keyword, pageable);

        Page<GroupResponseDto> groupResponseDtos = groups.map(group -> GroupResponseDto.builder()
            .id(group.getId())
            .name(group.getName())
            .city(group.getCity())
            .organization(group.getOrganization())
            .description(group.getDescription())
            .memberCount(groupMemberRepository.countByGroupId(group.getId()))
            .receiptCount(receiptRepository.countByGroup(group))
            .accountantName(findAccountant(group).getName())
            .build());

        return new GroupListResponseDto(groupResponseDtos);

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
        check(member, group);
        join(member, group, role);
    }

    @Transactional
    public void leaveGroup(String email, Long groupId) {
        Member member = memberRepository.getMemberByEmail(email);
        Group group = groupRepository.getGroupById(groupId);
        GroupMember groupMember = groupMemberRepository.getGroupMemberByGroupIdAndMemberId(groupId, member.getId());
        if (groupMember.getRole().equals(Role.ACCOUNTANT)) {
            throw new CustomException(ErrorStatus.ACCOUNTANT_CANNOT_LEAVE_GROUP, ErrorStatus.ACCOUNTANT_CANNOT_LEAVE_GROUP.getMessage());
        }
        groupMemberRepository.delete(groupMember);
        notificationService.createNotification(member, "그룹을 탈퇴했어요.", group.getName() + " 그룹을 탈퇴했어요. 다음에 또 만나요! 👋");
    }

    private Member findAccountant(Group group) {
        return groupMemberRepository.getGroupMemberByGroupIdAndRole(group.getId(), Role.ACCOUNTANT).getMember();
    }

    private void check(Member member, Group group) {
        
        // 회원이 속한 그룹의 수가 10개를 초과할 수 없다.
        if (groupMemberRepository.countByMemberId(member.getId()) >= 10) {
            throw new CustomException(ErrorStatus.TOO_MUCH_GROUPS, ErrorStatus.TOO_MUCH_GROUPS.getMessage());
        }

        // 이미 가입한 그룹에 다시 가입할 수 없다.
        if (groupMemberRepository.existsByGroupIdAndMemberId(group.getId(), member.getId())) {
            throw new CustomException(ErrorStatus.ALREADY_JOINED_GROUP, ErrorStatus.ALREADY_JOINED_GROUP.getMessage());
        }
    }

    private void join(Member member, Group group, Role role) {
        GroupMember groupMember = GroupMember.builder()
            .member(member)
            .group(group)
            .role(role)
            .build();
        groupMemberRepository.save(groupMember);
        notificationService.createNotification(member, "새로운 그룹에 가입했어요.", group.getName() + " 그룹에 가입을 축하드려요! 🎉");
    }

    @Transactional(readOnly = true)
    public MemberListResponseDto getGroupMembers(Long groupId, int page) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<GroupMember> groupMembers = groupMemberRepository.findByGroupId(groupId, pageable);

        Page<MemberResponseDto> memberResponseDtos = groupMembers.map(
            groupMember -> MemberResponseDto.of(groupMember.getMember())
        );

        return new MemberListResponseDto(memberResponseDtos);
    }


    @Transactional(readOnly = true)
    public ReceiptListResponseDto getReceiptListInGroup(String email, Long groupId, int page) {
        Member member = memberRepository.getMemberByEmail(email);
        Group group = groupRepository.getGroupById(groupId);
        GroupMember groupMember = groupMemberRepository.getGroupMemberByGroupIdAndMemberId(groupId, member.getId());
        Pageable pageable = PageRequest.of(page, 10);

//        // 회계는 그룹 내 모든 영수증을 조회할 수 있다.
//        if (groupMember.getRole().equals(Role.ACCOUNTANT)) {
//            Page<Receipt> receiptPage = receiptRepository.getReceiptListInGroup(group, pageable);
//            Page<ReceiptResponseDto> receiptResponseDtos = receiptPage.map(ReceiptResponseDto::of);
//            return new ReceiptListResponseDto(receiptResponseDtos);
//        }

        // 일반 사용자는 그룹 내 자신이 업로드한 영수증만 조회할 수 있다.
        Page<Receipt> receiptPage = receiptRepository.getReceiptListInGroup(member, group, pageable);
        Page<ReceiptResponseDto> receiptResponseDtos = receiptPage.map(ReceiptResponseDto::of);
        return new ReceiptListResponseDto(receiptResponseDtos);
    }
}
