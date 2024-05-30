package com.savemyreceipt.smr.oauth;

import com.savemyreceipt.smr.DTO.auth.TokenDto;
import com.savemyreceipt.smr.jwt.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    @Value("${spring.security.oauth2.client.send-redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        response.sendRedirect(redirectUri + "?accessToken=" + tokenDto.getAccessToken());
    }

}
