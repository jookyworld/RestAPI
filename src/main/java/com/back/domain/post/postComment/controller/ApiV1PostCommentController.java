package com.back.domain.post.postComment.controller;

import com.back.domain.post.post.dto.PostCommentDto;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.domain.post.postComment.entity.PostComment;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
public class ApiV1PostCommentController {
    private final PostService postService;

    @GetMapping()
    public List<PostCommentDto> getComments(@PathVariable long postId) {
        Post post = postService.getPost(postId);
        List<PostComment> comments = post.getComments();
        return comments
                .stream()
                .map(PostCommentDto::new)
                .toList();
    }

    @GetMapping("/{id}")
    public PostCommentDto getComment(@PathVariable("postId") long postId, @PathVariable("id") long id) {
        Post post = postService.getPost(postId);
        PostComment postComment = post.findCommentById(id).get();
        return new PostCommentDto(postComment);
    }
}
