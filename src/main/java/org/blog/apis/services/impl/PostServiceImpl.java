package org.blog.apis.services.impl;


import org.blog.apis.entities.Category;
import org.blog.apis.entities.Post;
import org.blog.apis.entities.User;
import org.blog.apis.exceptions.ResourceNotFoundException;
import org.blog.apis.payloads.PostRequestDto;
import org.blog.apis.payloads.PostResponseDto;
import org.blog.apis.repositories.CategoryRepo;
import org.blog.apis.repositories.PostRepo;
import org.blog.apis.repositories.UserRepositories;
import org.blog.apis.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepo postRepo;
    private final UserRepositories userRepositories;
    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;

    public PostServiceImpl(PostRepo postRepo, UserRepositories userRepositories, CategoryRepo categoryRepo, ModelMapper modelMapper) {
        this.postRepo = postRepo;
        this.userRepositories = userRepositories;
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
    }


    @Override
    public PostResponseDto createPost(PostRequestDto requestDto, Long userId, Long categoryId) {
        User user = userRepositories.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userid", userId));
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryid", categoryId));
        Post post = modelMapper.map(requestDto, Post.class);
        post.setPostDate(LocalDateTime.now());
        post.setUser(user);
        post.setCategory(category);
        Post savedPost = postRepo.save(post);
        return modelMapper.map(savedPost, PostResponseDto.class);
    }

    @Override
    public List<PostResponseDto> allPosts() {
        List<Post> allPost = postRepo.findAll();
        return allPost.stream().map(post -> modelMapper.map(post, PostResponseDto.class)).toList();
    }

    @Override
    public PostResponseDto getPostUsingId(Long id) {
        Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "postid", id));
        return modelMapper.map(post, PostResponseDto.class);
    }

    @Override
    public PostResponseDto updatePost(PostRequestDto postRequestDto, Long id) {
        Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "postid", id));
        modelMapper.map(postRequestDto, post);
        Post savedPost = postRepo.save(post);
        return modelMapper.map(savedPost, PostResponseDto.class);
    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "postid", id));
        postRepo.delete(post);
    }

    @Override
    public List<PostResponseDto> findPostUsingUserId(Long id) {
        User user = userRepositories.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "userid", id));
        List<Post> posts = postRepo.findByUser(user);
        return posts.stream().map(post -> modelMapper.map(post, PostResponseDto.class)).toList();

    }

    @Override
    public List<PostResponseDto> findPostUsingCategoryId(Long id) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("category", "categoryid", id));
        List<Post> posts = postRepo.findByCategory(category);
        return posts.stream().map(post -> modelMapper.map(post, PostResponseDto.class)).toList();
    }


}
