package com.sparta.instagramclone.shared;

import com.sparta.instagramclone.domain.Comment;
import com.sparta.instagramclone.domain.Member;
import com.sparta.instagramclone.domain.Post;
import com.sparta.instagramclone.handler.ex.*;
import com.sparta.instagramclone.jwt.JwtTokenProvider;
import com.sparta.instagramclone.repository.CommentRepository;
import com.sparta.instagramclone.repository.LikeRepository;
import com.sparta.instagramclone.repository.MemberRepository;
import com.sparta.instagramclone.repository.PostRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
@Slf4j
@Component
@RequiredArgsConstructor
public class Verification {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public void checkPost(Post post) {
        if (post == null) throw new NotFoundPostException();
    }

    public void checkComment(Comment comment) {
        if (comment == null) throw new NotFoundCommentException();
    }

    public void checkPostAuthor(Member member, Post post) {
        log.info(post.getMember().toString());
        log.info(member.toString());
        if (!post.getMember().equals(member)) throw new NotAuthorException();
    }

    public void checkCommentAuthor(Member member, Comment comment) {
        if (!comment.getMember().getId().equals(member.getId())) throw new NotAuthorException();
    }


    public void tokenCheck(HttpServletRequest request, Member member) {
        if (request.getHeader("Authorization") == null) throw new TokenExpiredException();
        if (member == null) throw new MemberNotFoundException();
    }

    @Transactional(readOnly = true)
    public Member getCurrentMember(Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        return optionalMember.orElse(null);
    }

    @Transactional(readOnly = true)
    public Post getCurrentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    @Transactional(readOnly = true)
    public Comment getCurrentComment(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        return optionalComment.orElse(null);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!jwtTokenProvider.validateToken(request.getHeader("Authorization").substring(7))) {
            throw new TokenExpiredException();
        }
        return jwtTokenProvider.getMemberFromAuthentication();
    }
}
