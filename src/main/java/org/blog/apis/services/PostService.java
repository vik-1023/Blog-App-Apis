package org.blog.apis.services;

import org.blog.apis.payloads.PostRequestDto;
import org.blog.apis.payloads.PostResponse;
import org.blog.apis.payloads.PostResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {

    PostResponseDto createPost(PostRequestDto requestDto, Long userId, Long categoryId);

    PostResponse getAllPosts(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir
    );

    List<PostResponseDto>searchPosts(String keyword);

    PostResponseDto getPostUsingId(Long id);

    PostResponseDto updatePost(PostRequestDto postRequestDto, Long id);

    void deletePost(Long id);

    List<PostResponseDto> findPostUsingUserId(Long id);


    List<PostResponseDto> findPostUsingCategoryId(Long id);

    PostResponseDto uploadPostImage(MultipartFile file, Long postId) throws IOException;

}
