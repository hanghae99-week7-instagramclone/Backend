package com.sparta.instagramclone.controller;

import com.sparta.instagramclone.dto.request.LikeAdressRequestDto;
import com.sparta.instagramclone.dto.response.ResponseDto;
import com.sparta.instagramclone.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class LikeController {
    private final LikeService likeService;

    @PostMapping("api/posts/{postId}/likes")
    public ResponseDto<?> Like(@PathVariable Long postId, HttpServletRequest request){
        return likeService.upDownLike(postId, request);
    }
}
