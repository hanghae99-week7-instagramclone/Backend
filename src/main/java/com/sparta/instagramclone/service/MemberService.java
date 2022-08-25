package com.sparta.instagramclone.service;

import com.sparta.instagramclone.domain.Follow;
import com.sparta.instagramclone.domain.Member;
import com.sparta.instagramclone.dto.JwtTokenDto;
import com.sparta.instagramclone.dto.request.LoginRequestDto;
import com.sparta.instagramclone.dto.request.MemberRequestDto;
import com.sparta.instagramclone.dto.response.MemberResponseDto;
import com.sparta.instagramclone.dto.response.ResponseDto;
import com.sparta.instagramclone.handler.ex.EmailNotFoundException;
import com.sparta.instagramclone.handler.ex.PasswordNotCollectException;
import com.sparta.instagramclone.jwt.JwtTokenProvider;
import com.sparta.instagramclone.repository.FollowRepository;
import com.sparta.instagramclone.repository.MemberRepository;
import com.sparta.instagramclone.shared.Verification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final FollowRepository followRepository;
    private final Verification verification;
    // 회원가입
    @Transactional
    public ResponseDto<?> registerMember(MemberRequestDto memberRequestDto) {
        if (!memberRequestDto.getPassword().equals(memberRequestDto.getPasswordConfirm())) {
            throw new PasswordNotCollectException();
        }

        Member member = Member.builder()
                .email(memberRequestDto.getEmail())
                .nickname(memberRequestDto.getNickname())
                .username(memberRequestDto.getUsername())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .build();
        memberRepository.save(member);

        return ResponseDto.success(new MemberResponseDto(member));
    }

    @Transactional
    public ResponseDto<?> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        Member member = memberRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(EmailNotFoundException::new);

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new PasswordNotCollectException();
        }

        JwtTokenDto tokenDto = jwtTokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);

        return ResponseDto.success(new MemberResponseDto(member));
    }

    public void tokenToHeaders(JwtTokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

    // 이메일 중복 체크
    @Transactional(readOnly = true)
    public boolean checkDuplicateEmail(String email) {
        if (memberRepository.countByEmail(email) != 0) {
            return false;
        }
        return true;
    }

    // 닉네임 중복 체크
    @Transactional(readOnly = true)
    public boolean checkDuplicateNickname(String nickname) {
        if (memberRepository.countByNickname(nickname) != 0) {
            return false;
        }
        return true;
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllMembers(HttpServletRequest request) {
        List<Member> memberList = memberRepository.findAll();
        List<MemberResponseDto> memberResponseDtoList = new ArrayList<>();
        Member currentMember = verification.validateMember(request);

        for (Member member : memberList) {
            Optional<Follow> optionalFollow = followRepository.findByFromMemberAndToMember_Id(currentMember, member.getId());
            boolean followByMe;
            followByMe = optionalFollow.isPresent();
            memberResponseDtoList.add(
                    MemberResponseDto.builder()
                            .id(member.getId())
                            .nickname(member.getNickname())
                            .profileUrl(member.getProfileUrl())
                            .followByMe(followByMe)
                            .createdAt(member.getCreatedAt())
                            .build()
            );
        }
        return ResponseDto.success(memberResponseDtoList);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getInfiniteMembers(Pageable pageable){
        PageRequest pageRequest = PageRequest.of(0,10,Sort.by(Sort.Direction.DESC, "id"));
        Slice<Member> page = memberRepository.findAll(pageRequest);
        List<MemberResponseDto> memberResponseDtoList = new ArrayList<>();
        for (Member member : page){
            memberResponseDtoList.add(
                    new MemberResponseDto(member)
            );
        }
        boolean hasNext = false;
        if (memberResponseDtoList.size() > pageable.getPageSize()) {
            memberResponseDtoList.remove(pageable.getPageSize());
            hasNext = true;
        }
        return ResponseDto.success(new SliceImpl<>(memberResponseDtoList, pageable, hasNext));
    }
}
