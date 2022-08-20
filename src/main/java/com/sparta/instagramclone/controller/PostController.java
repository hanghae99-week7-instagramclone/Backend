package com.sparta.instagramclone.controller;

import com.sparta.instagramclone.dto.request.PostRequestDto;
import com.sparta.instagramclone.dto.response.ResponseDto;
import com.sparta.instagramclone.repository.PostRepository;
import com.sparta.instagramclone.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;

    @PostMapping("/api/posts")
    public ResponseDto<?> createPost(@RequestPart(required = false) List<MultipartFile> multipartFile,
                                         @RequestPart PostRequestDto postRequestDto,
                                         HttpServletRequest request) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("MULTIPART FILE IS EMPTY");
        }
        return postService.createPost(multipartFile, postRequestDto, request);
    }

    @GetMapping("/api/posts")
    public ResponseDto<?> getAllPost() {
        return postService.getAllPosts();
    }

    @PutMapping("/api/posts/{postId}")
    public ResponseDto<?> updatePost(@PathVariable Long postId, @RequestPart(required = false) List<MultipartFile> multipartFile,
                                     @RequestPart PostRequestDto postRequestDto,
                                     HttpServletRequest request) throws IOException {
        return postService.updatePost(postId, multipartFile, postRequestDto, request);
    }

    @DeleteMapping("/api/posts/{postId}")
    public ResponseDto<?> deletePost(@PathVariable Long postId, HttpServletRequest request) {
        return postService.deletePost(postId, request);
    }
}
