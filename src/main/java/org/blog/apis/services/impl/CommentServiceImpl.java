package org.blog.apis.services.impl;

import org.blog.apis.entities.Comment;
import org.blog.apis.entities.Post;
import org.blog.apis.entities.User;
import org.blog.apis.exceptions.ResourceNotFoundException;
import org.blog.apis.payloads.CommentRequestDto;
import org.blog.apis.payloads.CommentResponseDto;
import org.blog.apis.repositories.CommentRepo;
import org.blog.apis.repositories.PostRepo;
import org.blog.apis.repositories.UserRepositories;
import org.blog.apis.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepo commentRepo;
    private final PostRepo postRepo;
    private final UserRepositories userRepositories;
    private final ModelMapper modelMapper;


    public CommentServiceImpl(CommentRepo commentRepo, PostRepo postRepo, UserRepositories userRepositories, ModelMapper modelMapper) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.userRepositories = userRepositories;
        this.modelMapper = modelMapper;
    }


    @Override
    public CommentResponseDto createComment(CommentRequestDto requestDto, Long userId, Long postId) {
        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post", "postId", postId));
        User user = userRepositories.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "userId", userId));
        Comment comment = modelMapper.map(requestDto, Comment.class);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setPost(post);
        comment.setUser(user);
        Comment savedComment = commentRepo.save(comment);
        CommentResponseDto response = modelMapper.map(savedComment, CommentResponseDto.class);
        response.setPostId(savedComment.getPost().getPostId());
        response.setUserId(savedComment.getUser().getId());
        return response;
    }

    @Override
    public List<CommentResponseDto> getCommentByPost(Long postId) {
        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post", "postId", postId));
        List<Comment> comments = commentRepo.findByPost(post);
        return comments.stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public CommentResponseDto updateComment(CommentRequestDto requestDto, Long commentId) {
        Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("comment", "commentId", commentId));
        comment.setContent(requestDto.getContent());
        Comment savedComponent = commentRepo.save(comment);

        return mapToResponse(savedComponent);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment deleteComment = commentRepo.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("comment", "commentId", commentId));
        commentRepo.delete(deleteComment);

    }

    private CommentResponseDto mapToResponse(Comment comment) {
        CommentResponseDto response = modelMapper.map(comment, CommentResponseDto.class);
        response.setPostId(comment.getPost().getPostId());
        response.setUserId(comment.getUser().getId());
        return response;
    }
}
