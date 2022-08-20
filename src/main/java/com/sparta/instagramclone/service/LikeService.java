package com.sparta.instagramclone.service;

import com.sparta.instagramclone.domain.Like;
import com.sparta.instagramclone.domain.Member;
import com.sparta.instagramclone.domain.Post;
import com.sparta.instagramclone.dto.request.LikeRequestDto;
import com.sparta.instagramclone.dto.response.ResponseDto;
import com.sparta.instagramclone.jwt.JwtTokenProvider;
import com.sparta.instagramclone.repository.LikeRepository;
import com.sparta.instagramclone.repository.MemberRepository;
import com.sparta.instagramclone.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikeService {
    private final JwtTokenProvider jwtTokenProvider;
    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public ResponseDto<?> upDownLike(Long postId, HttpServletRequest request) {
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        //postService 없어서 임시 구축
        Post post = isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        Like findLike = likeRepository.findByMemberAndPost(member,post).orElse(null);

        if(findLike == null){
            LikeRequestDto likerequestDto = new LikeRequestDto(member, post);
            Like like = new Like(likerequestDto);
            likeRepository.save(like);
            return ResponseDto.success(true);
        } else {
            likeRepository.deleteById(findLike.getId());
            return ResponseDto.success(false);
        }
    }

    @javax.transaction.Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!jwtTokenProvider.validateToken(request.getHeader("Authorization").substring(7))) {
            return null;
        }
        return jwtTokenProvider.getMemberFromAuthentication();
    }

    //임시 isPresentPost(postservice 구축시 이동)
    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }
}
