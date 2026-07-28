package org.blog.apis.controller;

import org.blog.apis.payloads.PostRequestDto;
import org.blog.apis.payloads.PostResponse;
import org.blog.apis.payloads.PostResponseDto;
import org.blog.apis.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/users/{user_id}/categories/{category_id}/posts")
    public ResponseEntity<PostResponseDto> create(@RequestBody PostRequestDto requestDto, @PathVariable Long user_id, @PathVariable Long category_id) {
        PostResponseDto createdPost = postService.createPost(requestDto, user_id, category_id);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "postId") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir
    ) {
        PostResponse allPost = postService.getAllPosts(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(allPost);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
        PostResponseDto post = postService.getPostUsingId(id);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> update(@RequestBody PostRequestDto postRequestDto, @PathVariable Long id) {
        PostResponseDto update = postService.updatePost(postRequestDto, id);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{id}/posts")
    public ResponseEntity<List<PostResponseDto>> getPostByUser(@PathVariable Long id) {
        List<PostResponseDto> post = postService.findPostUsingUserId(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/category/{id}/posts")
    public ResponseEntity<List<PostResponseDto>> getPostByCategory(@PathVariable Long id) {
        List<PostResponseDto> post = postService.findPostUsingCategoryId(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/posts/search/{keyword}")
    public ResponseEntity<List<PostResponseDto>> search(@PathVariable String keyword) {
        List<PostResponseDto> search = postService.searchPosts(keyword);
        return ResponseEntity.ok(search);
    }
}
