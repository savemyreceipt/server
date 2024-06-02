package com.savemyreceipt.smr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savemyreceipt.smr.DTO.member.request.MemberRequestDto;
import com.savemyreceipt.smr.DTO.member.response.MemberResponseDto;
import com.savemyreceipt.smr.exception.SuccessStatus;
import com.savemyreceipt.smr.infrastructure.GroupRepository;
import com.savemyreceipt.smr.infrastructure.MemberRepository;
import com.savemyreceipt.smr.infrastructure.ReceiptRepository;
import com.savemyreceipt.smr.oauth.OAuth2UserService;
import com.savemyreceipt.smr.service.GroupService;
import com.savemyreceipt.smr.service.MemberService;
import com.savemyreceipt.smr.service.ReceiptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void getMember_shouldReturnMemberDetails() throws Exception {
        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
            .id(1L)
            .email("testuser@example.com")
            .name("testuser")
            .profileUri("http://example.com/profile.jpg")
            .build();        // Populate memberResponseDto with test data
        when(memberService.getMember(anyString())).thenReturn(memberResponseDto);

        mockMvc.perform(get("/member")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(SuccessStatus.GET_MEMBER_SUCCESS.getHttpStatus().value()))
            .andExpect(jsonPath("$.message").value(SuccessStatus.GET_MEMBER_SUCCESS.getMessage()))
            .andExpect(jsonPath("$.data.name").value("testuser"))
            .andExpect(jsonPath("$.data.email").value("testuser@example.com"))
            .andExpect(jsonPath("$.data.profileUri").value("http://example.com/profile.jpg"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void updateMember_shouldUpdateMemberDetails() throws Exception {
        MemberRequestDto memberRequestDto = new MemberRequestDto();
        // Populate memberRequestDto with test data

        mockMvc.perform(put("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberRequestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(SuccessStatus.UPDATE_MEMBER_SUCCESS.getHttpStatus().value()))
            .andExpect(jsonPath("$.message").value(SuccessStatus.UPDATE_MEMBER_SUCCESS.getMessage()));
    }
}