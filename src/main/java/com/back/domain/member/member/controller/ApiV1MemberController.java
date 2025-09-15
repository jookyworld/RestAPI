package com.back.domain.member.member.controller;

import com.back.domain.member.member.dto.MemberDto;
import com.back.domain.member.member.dto.MemberJoinRequestBody;
import com.back.domain.member.member.dto.MemberLoginRequestBody;
import com.back.domain.member.member.dto.MemberLoginResponseBody;
import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.MemberService;
import com.back.global.exception.ServiceException;
import com.back.global.rsData.RsData;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/members")
@RestController
@RequiredArgsConstructor
@Tag(name = "ApiV1MemberController", description = "회원")
public class ApiV1MemberController {
    private final MemberService memberService;

    @PostMapping
    public RsData<MemberDto> join(@Valid @RequestBody MemberJoinRequestBody requestBody) {
        Member member = memberService.join(requestBody.username(), requestBody.password(), requestBody.nickname());

        return new RsData<>("201-1",
                "%s님 환영합니다. 회원가입이 완료되었습니다.".formatted(member.getNickname()), new MemberDto(member));
    }

    @PostMapping("/login")
    public RsData<MemberLoginResponseBody> login(@Valid @RequestBody MemberLoginRequestBody reqBody) {
        Member member = memberService.findByUsername(reqBody.username())
                .orElseThrow(() -> new ServiceException("401-1", "존재하지 않는 회원입니다."));

        if (!member.getPassword().equals(reqBody.password())) {
            throw new ServiceException("401-2", "비밀번호가 일치하지 않습니다.");
        }


        return new RsData<>(
                "200-1",
                "%s님 환영합니다.".formatted(member.getNickname()),
                new MemberLoginResponseBody(
                        new MemberDto(member),
                        member.getApiKey())
        );

    }

}
