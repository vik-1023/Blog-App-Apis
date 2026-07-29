package org.blog.apis.services.impl;


import org.blog.apis.entities.Category;
import org.blog.apis.entities.Post;
import org.blog.apis.entities.User;
import org.blog.apis.exceptions.ResourceNotFoundException;
import org.blog.apis.payloads.PostRequestDto;
import org.blog.apis.payloads.PostResponse;
import org.blog.apis.payloads.PostResponseDto;
import org.blog.apis.repositories.CategoryRepo;
import org.blog.apis.repositories.PostRepo;
import org.blog.apis.repositories.UserRepositories;
import org.blog.apis.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepo postRepo;
    private final UserRepositories userRepositories;
    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;
    private static final Logger logger =
            LoggerFactory.getLogger(PostServiceImpl.class);

    public PostServiceImpl(PostRepo postRepo, UserRepositories userRepositories, CategoryRepo categoryRepo, ModelMapper modelMapper) {
        this.postRepo = postRepo;
        this.userRepositories = userRepositories;
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
    }


    @Override
    public PostResponseDto createPost(PostRequestDto requestDto, Long userId, Long categoryId) {
        logger.info("creating post for userId :{} and categoryId :{}", userId, categoryId);
        User user = userRepositories.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        Post post = modelMapper.map(requestDto, Post.class);
        post.setPostDate(LocalDateTime.now());
        post.setUser(user);
        post.setCategory(category);
        Post savedPost = postRepo.save(post);
        logger.info("post created  successfully with postId :{}", savedPost.getPostId());
        return modelMapper.map(savedPost, PostResponseDto.class);
    }

    @Override
    public PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        logger.info("getting all posts using pagination and sorting");
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> pagePost = postRepo.findAll(pageable);
        List<Post> posts = pagePost.getContent();
        List<PostResponseDto> postDtos = posts.stream().map(post -> modelMapper.map(post, PostResponseDto.class)).toList();

        PostResponse response = new PostResponse();
        response.setContent(postDtos);
        response.setPageNumber(pagePost.getNumber());
        response.setPageSize(pagePost.getSize());
        response.setTotalElements(pagePost.getTotalElements());
        response.setTotalPages(pagePost.getTotalPages());
        response.setLastPage(pagePost.isLast());
        logger.info("All post fetched successfully");
        return response;
    }

    @Override
    public List<PostResponseDto> searchPosts(String keyword) {
        logger.info("Fetching posts using searchKeyword :{}", keyword);
        List<Post> posts = postRepo.findByPostTitleContainingIgnoreCase(keyword);
        logger.info("post fetched successfully related :{}", keyword);
        return posts.stream().map(post -> modelMapper.map(post, PostResponseDto.class)).toList();
    }


    @Override
    public PostResponseDto getPostUsingId(Long id) {
        logger.info("Fetching posts using id :{}", id);
        Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "postId", id));
        logger.info("Post fetched successfully with id: {}", id);
        return modelMapper.map(post, PostResponseDto.class);
    }

    @Override
    public PostResponseDto updatePost(PostRequestDto postRequestDto, Long id) {
        logger.info("Updating post with id: {}", id);
        Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "postId", id));
        modelMapper.map(postRequestDto, post);
        Post savedPost = postRepo.save(post);
        logger.info("Post updated successfully with id: {}", id);
        return modelMapper.map(savedPost, PostResponseDto.class);
    }

    @Override
    public void deletePost(Long id) {
        logger.info("Deleting post with id: {}", id);
        Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "postId", id));
        postRepo.delete(post);
        logger.info("Post deleted successfully with id: {}", id);
    }

    @Override
    public List<PostResponseDto> findPostUsingUserId(Long id) {
        logger.info("Post finding using userId : {}", id);
        User user = userRepositories.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "userId", id));
        List<Post> posts = postRepo.findByUser(user);
        logger.info("Post find successfully with userId: {}", id);
        return posts.stream().map(post -> modelMapper.map(post, PostResponseDto.class)).toList();

    }

    @Override
    public List<PostResponseDto> findPostUsingCategoryId(Long id) {
        logger.info("Post finding using categoryId : {}", id);
        Category category = categoryRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", id));
        List<Post> posts = postRepo.findByCategory(category);
        logger.info("Post find successfully with categoryId : {}", id);
        return posts.stream().map(post -> modelMapper.map(post, PostResponseDto.class)).toList();
    }


}
