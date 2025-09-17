package com.back.global.rq;

import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.MemberService;
import com.back.domain.post.post.service.PostService;
import com.back.global.exception.ServiceException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Rq {
    private final PostService postService;
    private final HttpServletRequest req;
    private final MemberService memberService;
    private final HttpServletResponse response;

    public Member getActor() {
        String headerAuthorization = getHeader("Authorization", "");

        String apiKey;

        if (!headerAuthorization.isBlank()) {
            if (!headerAuthorization.startsWith("Bearer ")) {
                throw new ServiceException("401-2", "인증 정보가 올바르지 않습니다.");
            }
            apiKey = headerAuthorization.substring("Bearer ".length()).trim();
        } else {
            apiKey = getCookieValue("apiKey", "");
        }

        if (apiKey.isBlank()) {
            throw new ServiceException("401-1", "로그인 후 사용해주세요.");
        }


        Member member = memberService.findByApiKey(apiKey)
                .orElseThrow(() -> new ServiceException("401-3", "회원을 찾을 수 없습니다."));

        return member;
    }

    private String getHeader(String name, String defaultValue) {
        return Optional
                .ofNullable(req.getHeader("Authorization"))
                .filter(headerValue -> !headerValue.isBlank())
                .orElse(defaultValue);
    }

    public void setCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    private String getCookieValue(String name, String defaultValue) {
        return Optional
                .ofNullable(req.getCookies())
                .flatMap(
                        cookies ->
                                Arrays.stream(req.getCookies())
                                        .filter(cookie -> name.equals(cookie.getName()))
                                        .map(Cookie::getValue)
                                        .findFirst()
                )
                .orElse(defaultValue);
    }

}
