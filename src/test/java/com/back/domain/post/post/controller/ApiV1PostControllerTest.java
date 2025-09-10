package com.back.domain.post.post.controller;

import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import org.hamcrest.Matchers;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        String createdDate = String.valueOf(post.getCreateDate());
        String modifiedDate = String.valueOf(post.getModifyDate());

        // 201 검증
        resultActions
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("write"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resultCode").value("201-1"))
                .andExpect(jsonPath("$.msg").value("%d번 게시글이 작성되었습니다.".formatted(post.getId())))
                .andExpect(jsonPath("$.data.id").value(post.getId()))
                .andExpect(jsonPath("$.data.createdDate").value(createdDate))
                .andExpect(jsonPath("$.data.modifiedDate").value(modifiedDate))
                .andExpect(jsonPath("$.data.subject").value("제목 new"))
                .andExpect(jsonPath("$.data.body").value("내용 new"));

    }

    @Test
    @DisplayName("글 수정")
    void t2() throws Exception {
        long id = 1;
        ResultActions resultActions = mvc.perform(
                put("/api/v1/posts/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "subject" : "제목 update",
                                    "body" : "내용 update"
                                }
                                """)
        ).andDo(print());

        // 200 OK 검증
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(jsonPath("$.resultCode").value("200-1"))
                .andExpect(jsonPath("$.msg").value("%d번 게시글이 수정되었습니다.".formatted(id)));
    }

    @Test
    @DisplayName("글 삭제")
    void t3() throws Exception{
        long id = 1;
        ResultActions resultActions = mvc.perform(
                delete("/api/v1/posts/" + id)
        ).andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("deleteItem"))
                .andExpect(jsonPath("$.resultCode").value("200-1"))
                .andExpect(jsonPath("$.msg").value("%d번 게시글이 삭제되었습니다.".formatted(id)));
    }

    @Test
    @DisplayName("단건 조회")
    void t4() throws Exception {
        ResultActions resultActions = mvc.perform(
                get("/api/v1/posts/3")
        ).andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("getItem"));
    }

    @Test
    @DisplayName("다건 조회")
    void t5() throws Exception {
        ResultActions resultActions = mvc.perform(
                get("/api/v1/posts")
        ).andDo(print());

        List<Post> posts = postService.getList();

        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("getItems"));


        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);

            resultActions
                    .andExpect(jsonPath("$[%d].id".formatted(i)).value(post.getId()))
                    .andExpect(jsonPath("$[%d].createdDate".formatted(i)).value(Matchers.startsWith(post.getCreateDate().toString().substring(0, 20))))
                    .andExpect(jsonPath("$[%d].modifiedDate".formatted(i)).value(Matchers.startsWith(post.getModifyDate().toString().substring(0, 20))))
                    .andExpect(jsonPath("$[%d].subject".formatted(i)).value(post.getTitle()))
                    .andExpect(jsonPath("$[%d].body".formatted(i)).value(post.getContent()));
        }
    }

    @Test
    @DisplayName("글 단건조회, 404")
    void t6() throws Exception {
        long id = 9999;

        //요청을 보냅니다.
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/posts/" + id)
                )
                .andDo(print()); // 응답을 출력합니다.

        // 200 Ok 상태코드 검증
        resultActions
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("getItem"))
                .andExpect(status().isNotFound());
    }



}
