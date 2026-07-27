package org.blog.apis.payloads;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDto {
    @NotBlank(message = "post title is required")
    private String postTitle;
    @NotBlank(message = "post content is required")
    private String postContent;
    private String postImage;
}
