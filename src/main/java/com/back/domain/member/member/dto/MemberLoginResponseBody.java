package com.back.domain.member.member.dto;

public record MemberLoginResponseBody(
        MemberDto item,
        String apiKey
) {

}
