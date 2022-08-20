package com.sparta.instagramclone.controller;

import com.sparta.instagramclone.dto.response.ResponseDto;
import com.sparta.instagramclone.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class FollowController {
    private final FollowService followService;

    @PostMapping("/api/follow/{toMemberId}")
    public ResponseDto<?> follow(@PathVariable Long toMemberId, HttpServletRequest request){
        return followService.upDownFollow(toMemberId, request);
    }
}
