package com.sparta.instagramclone.controller;

import com.sparta.instagramclone.dto.request.CommentRequestDto;
import com.sparta.instagramclone.dto.response.ResponseDto;
import com.sparta.instagramclone.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/posts/{postId}")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseDto<?> createComment (@RequestBody CommentRequestDto commentRequestDto, @PathVariable Long postId, HttpServletRequest request){
        return commentService.createComment(commentRequestDto, postId, request);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseDto<?> updateComment(@RequestBody CommentRequestDto commentRequestDto, @PathVariable Map<String, String> pathVarMap, HttpServletRequest request){
        Long postId = Long.parseLong(pathVarMap.get("postId"));
        Long commentId = Long.parseLong(pathVarMap.get("commentId"));
        return commentService.updateComment(commentRequestDto, postId, commentId, request);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseDto<?> deleteComment (@PathVariable Map<String, String> pathVarMap, HttpServletRequest request) {
        Long postId = Long.parseLong(pathVarMap.get("postId"));
        Long commentId = Long.parseLong(pathVarMap.get("commentId"));
        return commentService.deleteComment(postId,commentId, request);
    }
}
