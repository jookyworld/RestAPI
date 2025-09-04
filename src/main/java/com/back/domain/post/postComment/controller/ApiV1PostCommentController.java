package com.back.domain.post.postComment.controller;

import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.domain.post.postComment.dto.PostCommentDto;
import com.back.domain.post.postComment.entity.PostComment;
import com.back.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
public class ApiV1PostCommentController {
    private final PostService postService;

    @Transactional(readOnly = true)
    @GetMapping()
    public List<PostCommentDto> getComments(@PathVariable long postId) {
        Post post = postService.findById(postId);
        List<PostComment> comments = post.getComments();
        return comments
                .stream()
                .map(PostCommentDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public PostCommentDto getComment(@PathVariable("postId") long postId, @PathVariable("id") long id) {
        Post post = postService.findById(postId);
        PostComment postComment = post.findCommentById(id).get();
        return new PostCommentDto(postComment);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public RsData<Void> deleteItem(@PathVariable long postId,  @PathVariable long id) {
        Post post = postService.findById(postId);
        PostComment postComment = post.findCommentById(id).get();

        postService.deleteComment(post, postComment);

        return new RsData<>("200-1",
                "%d번 댓글이 삭제되었습니다.".formatted(id));
    }
}
