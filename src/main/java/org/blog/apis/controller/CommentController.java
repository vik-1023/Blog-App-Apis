package org.blog.apis.controller;

import jakarta.validation.Valid;
import org.blog.apis.payloads.CommentRequestDto;
import org.blog.apis.payloads.CommentResponseDto;
import org.blog.apis.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/posts/{postId}/users/{userId}/comments")
    public ResponseEntity<CommentResponseDto> create(@Valid @RequestBody CommentRequestDto request, @PathVariable Long userId, @PathVariable Long postId) {
        CommentResponseDto response = commentService.createComment(request, userId, postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long postId) {
        List<CommentResponseDto> getAll = commentService.getCommentByPost(postId);
        return ResponseEntity.ok(getAll);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> update(@Valid @RequestBody CommentRequestDto requestDto, @PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.updateComment(requestDto, commentId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }


}
