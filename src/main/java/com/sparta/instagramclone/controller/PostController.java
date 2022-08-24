package com.sparta.instagramclone.controller;

import com.sparta.instagramclone.dto.request.PostRequestDto;
import com.sparta.instagramclone.dto.response.ResponseDto;
import com.sparta.instagramclone.repository.PostRepository;
import com.sparta.instagramclone.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    //게시글 전체 조회
    @GetMapping("/api/posts")
    public ResponseDto<?> getAllPost(@RequestParam Long lastPostId) {
        return postService.getAllPosts(lastPostId);
    }
    //유저 게시글 조회
    @GetMapping("/api/posts/member/{memberId}")
    public ResponseDto<?> memberPost(@PathVariable Long memberId){
        return postService.getMemberPost(memberId);
    }

    //게시글 상세 조회
    @GetMapping("/api/posts/{postId}")
    public ResponseDto<?> detailPost(@PathVariable Long postId, HttpServletRequest request){
        return postService.getDetailPost(postId, request);

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
