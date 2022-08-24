package com.sparta.instagramclone.service;

import com.sparta.instagramclone.domain.Like;
import com.sparta.instagramclone.domain.Member;
import com.sparta.instagramclone.domain.Post;
import com.sparta.instagramclone.dto.request.LikeRequestDto;
import com.sparta.instagramclone.dto.response.ResponseDto;
import com.sparta.instagramclone.repository.LikeRepository;
import com.sparta.instagramclone.shared.Verification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final Verification verification;

    @Transactional
    public ResponseDto<?> upDownLike(Long postId, HttpServletRequest request) {
        Member member = verification.validateMember(request);
        verification.tokenCheck(request, member);
        Post post = verification.getCurrentPost(postId);
        verification.checkPost(post);

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

}
