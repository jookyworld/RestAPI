package com.back.domain.post.post.controller;

import com.back.domain.member.member.entity.Member;
import com.back.domain.post.post.dto.PostDto;
import com.back.domain.post.post.dto.PostModifyReqBody;
import com.back.domain.post.post.dto.PostWriteReqBody;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.global.rq.Rq;
import com.back.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController // @Controller + @ResponseBody
@RequestMapping("/api/v1/posts")
@Tag(name = "ApiV1PostController", description = "게시")
public class ApiV1PostController {
    private final PostService postService;
    private final Rq rq;

    @Transactional(readOnly = true)
    @GetMapping
    @Operation(summary = "전체 조회")
    public List<PostDto> getItems() {
        List<Post> items = postService.getList();
        return items
                .stream()
                .map(PostDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    @Operation(summary = "단건 조회")
    public PostDto getItem(@PathVariable long id) {
        Post post = postService.findById(id);
        return new PostDto(post);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "삭제")
    public RsData<Void> delete(@PathVariable long id) {
        Member actor = rq.getActor();

        Post post = postService.findById(id);

        post.checkActorCanDelete(actor);

        postService.delete(post);
        return new RsData<>("200-1", "%d번 게시글이 삭제되었습니다.".formatted(id));
    }

    @Transactional
    @PostMapping
    @Operation(summary = "작성")
    public RsData<PostDto> write(@Valid @RequestBody PostWriteReqBody reqBody) {

        System.out.println("rq.getActor: " + rq.getActor());
        Member actor = rq.getActor();

        Post post = postService.create(actor, reqBody.title(), reqBody.content());

        return new RsData<>(
                "201-1",
                "%d번 게시글이 작성되었습니다.".formatted(post.getId()),
                new PostDto(post)
        );
    }

    @PutMapping("{id}")
    @Transactional
    @Operation(summary = "수정")
    public RsData<Void> modify(@PathVariable long id,
                         @Valid @RequestBody PostModifyReqBody reqBody) {

        Member actor = rq.getActor();

        Post post = postService.findById(id);

        post.checkActorCanModify(actor);

        postService.update(post, reqBody.title(), reqBody.content());

        return new RsData<>(
                "200-1",
                "%d번 게시글이 수정되었습니다.".formatted(id)
        );
    }
}
