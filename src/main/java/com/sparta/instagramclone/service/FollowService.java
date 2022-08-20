package com.sparta.instagramclone.service;

import com.sparta.instagramclone.domain.Follow;
import com.sparta.instagramclone.domain.Member;
import com.sparta.instagramclone.dto.request.FollowRequestDto;
import com.sparta.instagramclone.dto.response.ResponseDto;
import com.sparta.instagramclone.jwt.JwtTokenProvider;
import com.sparta.instagramclone.repository.FollowRepository;
import com.sparta.instagramclone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Service
public class FollowService {
    private final JwtTokenProvider jwtTokenProvider;
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final LikeService likeService;

    @Transactional
    public ResponseDto<?> upDownFollow(Long toMemberId, HttpServletRequest request) {
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member fromMember = validateMember(request);
        if (null == fromMember) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Member toMember = memberRepository.findById(toMemberId).orElse(null);
        if (null == toMember){
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 사용자 id 입니다.");
        }

        Follow findFollowing = followRepository.findByFromMemberAndToMember_Id(fromMember,toMemberId).orElse(null);



        if(findFollowing == null){
            FollowRequestDto followRequestDto = new FollowRequestDto(fromMember, toMember);
            Follow follow = new Follow(followRequestDto);
            followRepository.save(follow);
            return ResponseDto.success(true);
        } else {
            followRepository.deleteById(findFollowing.getId());
            return ResponseDto.success(false);
        }
    }
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!jwtTokenProvider.validateToken(request.getHeader("Authorization").substring(7))) {
            return null;
        }
        return jwtTokenProvider.getMemberFromAuthentication();
    }
}
