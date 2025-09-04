package com.back.domain.post.postComment.controller;

import com.back.domain.post.postComment.dto.PostCommentDto;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.domain.post.postComment.entity.PostComment;
import com.back.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/{id}/delete")
    public RsData deleteItem(@PathVariable long postId,  @PathVariable long id) {
        Post post = postService.findById(postId);
        PostComment postComment = post.findCommentById(id).get();

        postService.deleteComment(post, postComment);

        Map<String, Object> rsData = new LinkedHashMap<>();
        rsData.put("resultCode", "200-1");
        rsData.put("msg", "%d번 댓글이 삭제되었습니다.".formatted(id));

        return new RsData("200-1",
                "%d번 댓글이 삭제되었습니다.".formatted(id),
                new PostCommentDto(postComment));
    }
}
