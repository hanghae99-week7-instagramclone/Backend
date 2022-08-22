package com.sparta.instagramclone.controller;

import com.sparta.instagramclone.dto.request.LoginRequestDto;
import com.sparta.instagramclone.dto.request.MemberRequestDto;
import com.sparta.instagramclone.dto.response.ResponseDto;
import com.sparta.instagramclone.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/members/signup")
    public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto memberRequestDto) {
        return memberService.registerMember(memberRequestDto);
    }

    @PostMapping("/members/login")
    public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto loginRequestDto,
                                HttpServletResponse response
    ) {
        return memberService.login(loginRequestDto, response);
    }

    @GetMapping("/members")
    public ResponseDto<?> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/members/email-check")
    public ResponseDto<?> checkDuplicateEmail(String email) {
        return ResponseDto.success(memberService.checkDuplicateEmail(email));
    }

    @GetMapping("/members/nickname-check")
    public ResponseDto<?> checkDuplicateNickname(String nickname) {
        return ResponseDto.success(memberService.checkDuplicateNickname(nickname));
    }
}
