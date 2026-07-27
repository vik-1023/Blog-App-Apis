package org.blog.apis.services;

import org.blog.apis.payloads.PostRequestDto;
import org.blog.apis.payloads.PostResponseDto;

import java.util.List;

public interface PostService {

    PostResponseDto createPost(PostRequestDto requestDto, Long userId, Long categoryId);

    List<PostResponseDto> allPosts();

    PostResponseDto getPostUsingId(Long id);

    PostResponseDto updatePost(PostRequestDto postRequestDto, Long id);

    void deletePost(Long id);

    List<PostResponseDto> findPostUsingUserId(Long id);


    List<PostResponseDto> findPostUsingCategoryId(Long id);

}
