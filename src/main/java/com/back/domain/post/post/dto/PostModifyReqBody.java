package com.back.domain.post.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostModifyReqBody(
        @NotBlank
        @Size(min = 2, max = 100)
        String subject,

        @NotBlank
        @Size(min = 2, max = 100)
        String body
) { }