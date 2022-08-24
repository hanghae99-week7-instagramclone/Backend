package com.sparta.instagramclone.service;

import com.sparta.instagramclone.domain.Follow;
import com.sparta.instagramclone.domain.Member;
import com.sparta.instagramclone.dto.request.FollowRequestDto;
import com.sparta.instagramclone.dto.response.ResponseDto;
import com.sparta.instagramclone.repository.FollowRepository;
import com.sparta.instagramclone.repository.MemberRepository;
import com.sparta.instagramclone.shared.Verification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Service
public class FollowService {
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final Verification verification;

    @Transactional
    public ResponseDto<?> upDownFollow(Long toMemberId, HttpServletRequest request) {
        Member fromMember = verification.validateMember(request);
        verification.tokenCheck(request, fromMember);

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

}
