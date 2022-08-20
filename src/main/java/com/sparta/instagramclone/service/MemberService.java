package com.sparta.instagramclone.service;

import com.sparta.instagramclone.domain.Member;
import com.sparta.instagramclone.dto.JwtTokenDto;
import com.sparta.instagramclone.dto.request.LoginRequestDto;
import com.sparta.instagramclone.dto.request.MemberRequestDto;
import com.sparta.instagramclone.dto.response.MemberResponseDto;
import com.sparta.instagramclone.dto.response.ResponseDto;
import com.sparta.instagramclone.handler.ex.*;
import com.sparta.instagramclone.jwt.JwtTokenProvider;
import com.sparta.instagramclone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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

    // 회원가입
    @Transactional
    public ResponseDto<?> registerMember(MemberRequestDto memberRequestDto) {
        String password = passwordEncoder.encode(memberRequestDto.getPassword());

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
        if (email.isEmpty()) {
            throw new IllegalArgumentException("이메일을 입력해주세요.");
        }
        if (memberRepository.countByEmail(email) != 0) {
            throw new DuplicateEmailException();
        }
        return true;
    }

    // 닉네임 중복 체크
    @Transactional(readOnly = true)
    public boolean checkDuplicateNickname(String nickname) {
        if (nickname.isEmpty()) {
            throw new IllegalArgumentException("닉네임을 입력해주세요.");
        }
        if (memberRepository.countByNickname(nickname) != 0) {
            throw new DuplicateNicknameException();
        }
        return true;
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllMembers() {
        List<Member> memberList = memberRepository.findAll();
        List<MemberResponseDto> memberResponseDtoList = new ArrayList<>();
        for (Member member : memberList) {
            memberResponseDtoList.add(
                    new MemberResponseDto(member)
            );
        }
        return ResponseDto.success(memberResponseDtoList);
    }
}
