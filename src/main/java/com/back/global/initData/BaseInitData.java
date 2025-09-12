package com.back.global.initData;

import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.MemberService;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Configuration
public class BaseInitData {

    @Autowired
    @Lazy
    private BaseInitData self;

    private final PostService postService;
    private final MemberService memberService;

    @Bean
    ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            self.work1();
            self.work2();
        };
    }

    @Transactional
    public void work1() {
        if(memberService.count() > 0 ) return;

        memberService.join("user1", "1234", "유저1");
        memberService.join("user2", "1234", "유저2");
        memberService.join("user3", "1234", "유저3");
        memberService.join("user4", "1234", "유저4");
        memberService.join("user5", "1234", "유저5");
    }

    @Transactional
    public void work2() {
        if(postService.count() > 0) return;

        Member member1 = memberService.findByUsername("user1");
        Member member2 = memberService.findByUsername("user2");
        Member member3 = memberService.findByUsername("user3");

        Post post1 = postService.create(member1, "제목 1", "내용 1");
        Post post2 = postService.create(member1, "제목 2", "내용 2");
        Post post3 = postService.create(member2, "제목 3", "내용 3");
        Post post4 = postService.create(member2, "제목 4", "내용 4");
        Post post5 = postService.create(member3, "제목 5", "내용 5");
        Post post6 = postService.create(member3, "제목 6", "내용 6");



        post1.addComment(member1, "댓글 1-1");
        post1.addComment(member2, "댓글 1-2");
        post1.addComment(member3, "댓글 1-3");
        post2.addComment(member1, "댓글 2-1");
        post2.addComment(member2, "댓글 2-2");
        post3.addComment(member3, "댓글 3-1");
        post3.addComment(member1, "댓글 3-2");
        post4.addComment(member2, "댓글 4-2");
        post4.addComment(member3, "댓글 4-2");
        post5.addComment(member3, "댓글 5-1");
    }


    @Transactional
    public void work3() {

    }

}
