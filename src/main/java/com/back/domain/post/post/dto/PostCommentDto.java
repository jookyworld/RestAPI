package com.back.domain.post.post.dto;

import com.back.domain.post.postComment.entity.PostComment;

import java.time.LocalDateTime;


public record PostCommentDto(
        long id,
        LocalDateTime createDate,
        LocalDateTime modifyDate,
        String body
) {
    public PostCommentDto(PostComment postComment) {
        this(
                postComment.getId(),
                postComment.getCreateDate(),
                postComment.getModifyDate(),
                postComment.getContent()
        );
    }

}
