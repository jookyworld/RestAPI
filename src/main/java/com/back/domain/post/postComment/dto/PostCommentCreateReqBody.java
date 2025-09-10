package com.back.domain.post.postComment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostCommentCreateReqBody(
        @NotBlank
        @Size(min = 2, max = 100)
        String content
) {

}
