package com.back.domain.post.post.controller;

import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController // @Controller + @ResponseBody
@RequestMapping("/api/v1/posts")
public class ApiV1PostController {
    private final PostService postService;

    @GetMapping
    public List<Post> getItems() {
        List<Post> items = postService.getList();
        return items;
    }

    @GetMapping("/{id}")
    public Post getItem(@PathVariable long id) {
        return postService.getPost(id);
    }
}
