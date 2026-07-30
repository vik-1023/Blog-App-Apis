package org.blog.apis.services;

import org.blog.apis.payloads.CommentRequestDto;
import org.blog.apis.payloads.CommentResponseDto;

import java.util.List;

public interface CommentService {
    CommentResponseDto createComment(CommentRequestDto requestDto, Long userId, Long postId);

    List<CommentResponseDto> getCommentByPost(Long postId);

    CommentResponseDto updateComment(CommentRequestDto requestDto, Long commentId);

    void deleteComment(Long commentId);
}
