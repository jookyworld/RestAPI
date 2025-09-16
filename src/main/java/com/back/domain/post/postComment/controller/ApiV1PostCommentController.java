package com.back.domain.post.postComment.controller;

import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.MemberService;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.domain.post.postComment.dto.PostCommentCreateReqBody;
import com.back.domain.post.postComment.dto.PostCommentDto;
import com.back.domain.post.postComment.dto.PostCommentModifyReqBody;
import com.back.domain.post.postComment.entity.PostComment;
import com.back.global.rq.Rq;
import com.back.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
@Tag(name = "ApiV1PostCommentController", description = "댓글 컨트롤러")
public class ApiV1PostCommentController {
    private final PostService postService;
    private final MemberService memberService;
    private final Rq rq;

    @Transactional(readOnly = true)
    @GetMapping()
    @Operation(summary = "댓글 전체 조회")
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
    @Operation(summary = "댓글 단건 조회")
    public PostCommentDto getComment(@PathVariable("postId") long postId, @PathVariable("id") long id) {
        Post post = postService.findById(postId);
        PostComment postComment = post.findCommentById(id).get();
        return new PostCommentDto(postComment);
    }

    @Transactional
    @DeleteMapping("/{id}")
    @Operation(summary = "댓글 삭제")
    public RsData<Void> deleteItem(@PathVariable long postId, @PathVariable long id) {
        Member actor = rq.getActor();

        Post post = postService.findById(postId);
        PostComment postComment = post.findCommentById(id).get();

        postComment.checkActorCanDeleteComment(actor);

        postService.deleteComment(post, postComment);

        return new RsData<>("200-1",
                "%d번 댓글이 삭제되었습니다.".formatted(id));
    }

    @Transactional
    @PutMapping("/{id}")
    @Operation(summary = "댓글 수정")
    public RsData<Void> modify(@PathVariable long postId, @PathVariable long id,
                         @Valid @RequestBody PostCommentModifyReqBody reqBody) {
        Member actor = rq.getActor();

        Post post = postService.findById(postId);
        PostComment postComment = post.findCommentById(id).get();

        postComment.checkActorCanModifyComment(actor);

        postService.modifyComment(postComment, reqBody.content());

        return new RsData<>(
                "200-1",
                "%d번 댓글 수정".formatted(id)
        );
    }

    @Transactional
    @PostMapping
    @Operation(summary = "댓글 작성")
    public RsData<PostCommentDto> write(@PathVariable long postId,
                                         @Valid @RequestBody PostCommentCreateReqBody body) {
        Member actor = rq.getActor();

        Post post = postService.findById(postId);

        PostComment postComment = postService.createComment(actor, post, body.content());

        postService.flush();

        return new RsData<>(
                "201-1",
                "%d번 댓글 작성".formatted(postComment.getId()),
                new PostCommentDto(postComment)
        );
    }
}
