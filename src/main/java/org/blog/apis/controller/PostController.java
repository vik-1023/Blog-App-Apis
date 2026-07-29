package org.blog.apis.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.blog.apis.payloads.PostRequestDto;
import org.blog.apis.payloads.PostResponse;
import org.blog.apis.payloads.PostResponseDto;
import org.blog.apis.services.ImageService;
import org.blog.apis.services.PostService;
import org.blog.apis.utils.AppConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    private ImageService imageService;

    @Value("${project.image}")
    private String path;

    public PostController(PostService postService, ImageService imageService) {
        this.postService = postService;
        this.imageService = imageService;
    }

    @PostMapping("/users/{user_id}/categories/{category_id}/posts")
    public ResponseEntity<PostResponseDto> create(@RequestBody PostRequestDto requestDto, @PathVariable Long user_id, @PathVariable Long category_id) {
        PostResponseDto createdPost = postService.createPost(requestDto, user_id, category_id);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getAllPosts(@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber, @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize, @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy, @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR) String sortDir) {
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

    @PostMapping("/posts/image/upload/{postId}")
    public ResponseEntity<PostResponseDto> uploadPostImage(@RequestParam("image") MultipartFile image, @PathVariable Long postId) throws IOException {

        PostResponseDto response = postService.uploadPostImage(image, postId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/posts/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable String imageName,
            HttpServletResponse response) throws IOException {

        InputStream resource = imageService.getResource(path, imageName);

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(resource, response.getOutputStream());
    }

}
