package com.back.domain.post.post.controller;

import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc   // MockMvc auto injection
@Transactional
public class ApiV1PostControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PostService postService;

    @Test
    @DisplayName("글쓰기")
    void t1() throws Exception {
        ResultActions resultActions = mvc.perform(
                post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title" : "제목 new",
                                    "content" : "내용 new"
                                }
                                """)
        ).andDo(print());

        Post post = postService.findLatest().get();
        long totalCount = postService.count();

        // 201 검증
        resultActions
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("write"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resultCode").value("201-1"))
                .andExpect(jsonPath("$.msg").value("%d번 게시글이 작성되었습니다.".formatted(post.getId())))
                .andExpect(jsonPath("$.data.totalCount").value(totalCount))
                .andExpect(jsonPath("$.data.post.id").value(post.getId()));

    }

    @Test
    @DisplayName("글 수정")
    void t2() throws Exception {
        ResultActions resultActions = mvc.perform(
                put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "subject" : "제목 update",
                                    "body" : "내용 update"
                                }
                                """)
        ).andDo(print());

        // 200 OK 검증
        resultActions.andExpect(status().isOk());

    }

}
