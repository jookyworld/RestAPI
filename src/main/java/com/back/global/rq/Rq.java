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

@Component
@RequiredArgsConstructor
public class Rq {
    private final PostService postService;
    private final HttpServletRequest req;
    private final MemberService memberService;
    private final HttpServletResponse response;

    public Member getActor() {
        String headerAuthorization =  req.getHeader("Authorization");

        String apiKey;

        if (headerAuthorization != null && !headerAuthorization.isBlank()) {
            if (!headerAuthorization.startsWith("Bearer ")) {
                throw new ServiceException("401-2", "인증 정보가 올바르지 않습니다.");
            }
            apiKey = headerAuthorization.substring("Bearer ".length()).trim();
        } else {
            apiKey = req.getCookies() == null ?
                    "" :
                    Arrays.stream(req.getCookies())
                            .filter(cookie -> "apiKey".equals(cookie.getName()))
                            .map(Cookie::getValue)
                            .findFirst().orElse("");
        }

        if (apiKey.isBlank()) {
            throw new ServiceException("401-1", "로그인 후 사용해주세요.");
        }


        Member member = memberService.findByApiKey(apiKey)
                .orElseThrow(() -> new ServiceException("401-3", "회원을 찾을 수 없습니다."));

        return member;
    }

    public void setCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
