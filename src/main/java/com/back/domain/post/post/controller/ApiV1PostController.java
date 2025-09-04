package com.back.domain.post.post.controller;

import com.back.domain.post.post.dto.PostDto;
import com.back.domain.post.post.dto.PostWriteReqBody;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController // @Controller + @ResponseBody
@RequestMapping("/api/v1/posts")
public class ApiV1PostController {
    private final PostService postService;

    @Transactional(readOnly = true)
    @GetMapping
    public List<PostDto> getItems() {
        List<Post> items = postService.getList();
        return items
                .stream()
                .map(PostDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public PostDto getItem(@PathVariable long id) {
        Post post = postService.findById(id);
        return new PostDto(post);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public RsData<Void> deleteItem(@PathVariable long id) {
        Post post = postService.findById(id);
        postService.delete(post);
        return new RsData<>("200-1", "%d번 게시글이 삭제되었습니다.".formatted(id));
    }

    @Transactional
    @PostMapping
    public RsData<PostDto> write(@Valid @RequestBody PostWriteReqBody form) {

        Post post = postService.create(form.title(), form.content());

        return new RsData<>("200-1", "%d번 게시글이 생성되었습니다.".formatted(post.getId())
                , new PostDto(post));
    }
}
