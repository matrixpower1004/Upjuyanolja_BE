package com.backoffice.upjuyanolja.domain.member.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

import com.backoffice.upjuyanolja.domain.member.dto.request.EmailRequest;
import com.backoffice.upjuyanolja.domain.member.dto.response.CheckEmailDuplicateResponse;
import com.backoffice.upjuyanolja.domain.member.dto.response.MemberInfoResponse;
import com.backoffice.upjuyanolja.domain.member.service.MemberAuthService;
import com.backoffice.upjuyanolja.domain.member.service.MemberGetService;
import com.backoffice.upjuyanolja.global.security.SecurityUtil;
import com.backoffice.upjuyanolja.global.util.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class MemberAuthControllerDocsTest extends RestDocsSupport {

    @MockBean
    private MemberAuthService memberAuthService;

    @MockBean
    private MemberGetService memberGetService;

    @MockBean
    private SecurityUtil securityUtil;

    @Test
    @DisplayName("checkEmailDuplicate()는 이메일 중복 검사를 할 수 있다.")
    void checkEmailDuplicate() throws Exception {
        // given
        EmailRequest request = EmailRequest.builder()
            .email("test@mail.com")
            .build();
        CheckEmailDuplicateResponse checkEmailDuplicateResponse = CheckEmailDuplicateResponse.builder()
            .isExists(true)
            .build();

        given(memberAuthService.checkEmailDuplicate(any(EmailRequest.class)))
            .willReturn(checkEmailDuplicateResponse);

        // when then
        mockMvc.perform(post("/api/auth/members/email")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(restDoc.document(
                requestFields(
                    fieldWithPath("email").description("이메일")
                ),
                responseFields(
                    fieldWithPath("isExists").type(JsonFieldType.BOOLEAN)
                        .description("이메일 중복 여부")
                )
            ));
    }

    @Test
    @DisplayName("getMember()는 회원 정보를 조회할 수 있다.")
    void getMember() throws Exception {
        // given
        MemberInfoResponse memberInfoResponse = MemberInfoResponse.builder()
            .memberId(1L)
            .email("test@mail.com")
            .name("test")
            .phoneNumber("010-1234-1234")
            .build();

        given(securityUtil.getCurrentMemberId()).willReturn(1L);
        given(memberGetService.getMember(any(Long.TYPE)))
            .willReturn(memberInfoResponse);

        // when then
        mockMvc.perform(get("/api/auth/members"))
            .andDo(restDoc.document(
                responseFields(
                    fieldWithPath("memberId").type(JsonFieldType.NUMBER)
                        .description("회원 식별자"),
                    fieldWithPath("email").type(JsonFieldType.STRING)
                        .description("이메일"),
                    fieldWithPath("name").type(JsonFieldType.STRING)
                        .description("이름"),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                        .description("전화번호")
                )
            ));

        verify(memberGetService, times(1)).getMember(any(Long.TYPE));
    }
}