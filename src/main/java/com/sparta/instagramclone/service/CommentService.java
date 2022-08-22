package com.sparta.instagramclone.service;

import com.sparta.instagramclone.domain.Comment;
import com.sparta.instagramclone.domain.Member;
import com.sparta.instagramclone.domain.Post;
import com.sparta.instagramclone.dto.request.CommentRequestDto;
import com.sparta.instagramclone.dto.response.CommentResponseDto;
import com.sparta.instagramclone.dto.response.ResponseDto;
import com.sparta.instagramclone.repository.CommentRepository;
import com.sparta.instagramclone.shared.Verification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final Verification verification;

    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto commentRequestDto, Long postId, HttpServletRequest request){
        Member member = verification.validateMember(request);
        verification.tokenCheck(request, member);
        Post post = verification.getCurrentPost(postId);
        verification.checkPost(post);

        Comment comment = Comment.builder()
                .content(commentRequestDto.getContent())
                .member(member)
                .post(post)
                .build();
        commentRepository.save(comment);
        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .postId(post.getId())
                        .memberId(member.getId())
                        .nickname(member.getNickname())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> getComments(Long postId) {
        Post post = verification.getCurrentPost(postId);
        verification.checkPost(post);

        List<Comment> commentList = commentRepository.findAllByPostId(postId);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentResponseDtoList.add(CommentResponseDto.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .modifiedAt(comment.getModifiedAt())
                    .postId(comment.getPost().getId())
                    .memberId(comment.getMember().getId())
                    .nickname(comment.getMember().getNickname())
                    .build());
        }
        return ResponseDto.success(commentResponseDtoList);
    }

    @Transactional
    public ResponseDto<?> updateComment (CommentRequestDto commentRequestDto, Long postId, Long commentId, HttpServletRequest request){
        Member member = verification.validateMember(request);
        verification.tokenCheck(request, member);
        Post post = verification.getCurrentPost(postId);
        verification.checkPost(post);
        Comment comment = verification.getCurrentComment(commentId);
        verification.checkComment(comment);
        verification.checkCommentAuthor(member, comment);

        comment.update(commentRequestDto);
        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .postId(post.getId())
                        .memberId(member.getId())
                        .nickname(member.getNickname())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> deleteComment(Long postId, Long commentId, HttpServletRequest request){
        Member member = verification.validateMember(request);
        verification.tokenCheck(request, member);
        Post post = verification.getCurrentPost(postId);
        verification.checkPost(post);
        Comment comment = verification.getCurrentComment(commentId);
        verification.checkComment(comment);
        verification.checkCommentAuthor(member, comment);

        commentRepository.delete(comment);
        return ResponseDto.success(true);
    }

}
