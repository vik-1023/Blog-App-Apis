package org.blog.apis.payloads;


import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostResponseDto {
    private Long postId;
    private String postTitle;
    private String postContent;
    private String postImage;
    private LocalDateTime postDate;
    private CategoryResponseDto category;
    private UserResponseDto user;
}
