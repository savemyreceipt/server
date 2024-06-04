package com.savemyreceipt.smr.oauth;

import com.savemyreceipt.smr.domain.Member;
import com.savemyreceipt.smr.exception.ErrorStatus;
import com.savemyreceipt.smr.exception.model.CustomException;
import com.savemyreceipt.smr.infrastructure.MemberRepository;
import com.savemyreceipt.smr.service.NotificationService;
import com.savemyreceipt.smr.utils.SendGridUtil;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final NotificationService notificationService;
    private final SendGridUtil sendGridUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
            .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        String email = oAuth2User.getAttribute("email");

        Member member = getMember(attributes, email);

        return new CustomOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(member.getAuthority().name())),
            oAuth2User.getAttributes(),
            attributes.getNameAttributeKey(),
            member.getAuthority(),
            attributes.getOAuth2UserInfo().getId()
        );
    }

    private Member getMember(OAuthAttributes attributes, String email) {
        Member member = memberRepository.findByOauth2Id(attributes.getOAuth2UserInfo().getId()).orElse(null);
        if (member == null) {
            return saveMember(attributes);
        }
        return member;
    }

    private Member saveMember(OAuthAttributes attributes) {
        Member member = attributes.toEntity(attributes.getOAuth2UserInfo());
        memberRepository.save(member);
        notificationService.createNotification(member, "회원가입 완료", member.getName()+ "님, 만나서 반가워요! 👋");
        try {
            sendGridUtil.sendSignupEmail(member);
        } catch (IOException e) {
            throw new CustomException(ErrorStatus.EMAIL_SEND_FAILED, ErrorStatus.EMAIL_SEND_FAILED.getMessage());
        }
        return member;
    }
}
