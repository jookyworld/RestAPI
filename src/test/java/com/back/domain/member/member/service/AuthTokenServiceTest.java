package com.back.domain.member.member.service;

import com.back.domain.member.member.entity.Member;
import com.back.standard.util.Ut;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class AuthTokenServiceTest {
    @Autowired
    private AuthTokenService authTokenService;
    @Autowired
    private MemberService memberService;
    @Value("${custom.jwt.secretKey}")
    private String jwtSecretKey;
    @Value("${custom.accessToken.expireSeconds}")
    private int accessTokenExpireSeconds;

    @Test
    @DisplayName("authTokenService 존재여부")
    void t1() {
        assertThat(authTokenService).isNotNull();
    }

    @Test
    @DisplayName("jjwt 최신 방식으로 jwt 생성, {name= \"Paul\", age=23}")
    void t2() {
        byte[] keyBytes = jwtSecretKey.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

        // 클레임 - 사용자 정보
        Map<String, Object> claims = Map.of("name", "Paul", "age", "23");

        // 생성시간, 만료시간 설정
        long expireMillis = 1000L * accessTokenExpireSeconds;     //토큰 만료 기간
        Date issueAt = new Date();  //발행 시간
        Date expiration = new Date(issueAt.getTime() + expireMillis); //만료 시간

        String jwt = Jwts.builder()
                .claims(claims)
                .issuedAt(issueAt)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();

        assertThat(jwt).isNotBlank();
        System.out.println("jwt : " + jwt);

        assertThat(Ut.jwt.isValid(jwtSecretKey, jwt)).isTrue();
        Map<String, Object> parsedPayload = Ut.jwt.payload(jwtSecretKey, jwt);
        assertThat(parsedPayload).containsAllEntriesOf(claims);

    }

    @Test
    @DisplayName("Ut.jwt.toString 통해서 jwt 생성, {name= \"Paul\", age=23}")
    void t3 () {
        Map<String, Object> claims = Map.of("name", "asdfasdf", "age", "100");

        String jwt = Ut.jwt.toString(
                jwtSecretKey,
                accessTokenExpireSeconds,
                claims
        );

        assertThat(jwt).isNotBlank();

        System.out.println("jwt : " + jwt);
        assertThat(Ut.jwt.isValid(jwtSecretKey, jwt)).isTrue();
        Map<String, Object> parsedPayload = Ut.jwt.payload(jwtSecretKey, jwt);
        assertThat(parsedPayload).containsAllEntriesOf(claims);
    }

    @Test
    @DisplayName("authTokenService.genAccessToken(member);")
    void t4 () {
        Member member = memberService.findByUsername("user1").get();
        String accessToken = authTokenService.genAccessToken(member);

        assertThat(accessToken).isNotBlank();

        System.out.println("accessToken : " + accessToken);

        Map<String, Object> parsedPayload = authTokenService.payload(accessToken);

        assertThat(parsedPayload)
                .containsAllEntriesOf(
                        Map.of(
                                "id", member.getId(),
                                "username", member.getUsername()
                        )
                );
    }
}
