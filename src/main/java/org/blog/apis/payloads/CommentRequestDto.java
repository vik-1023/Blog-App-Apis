package org.blog.apis.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    @NotBlank(message = "comment content is required")
    @Size(min = 2, max = 500, message = "comment must be between 2  and 500 character")
    private String content;
}
