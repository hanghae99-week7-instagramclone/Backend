package com.sparta.instagramclone.controller;

import com.sparta.instagramclone.dto.request.LoginRequestDto;
import com.sparta.instagramclone.dto.request.MemberRequestDto;
import com.sparta.instagramclone.dto.response.ResponseDto;
import com.sparta.instagramclone.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseDto<?> getAllMembers(HttpServletRequest request) {
        return memberService.getAllMembers(request);
    }

    @GetMapping("/members/email-check")
    public ResponseDto<?> checkDuplicateEmail(String email) {
        return ResponseDto.success(memberService.checkDuplicateEmail(email));
    }

    @GetMapping("/members/nickname-check")
    public ResponseDto<?> checkDuplicateNickname(String nickname) {
        return ResponseDto.success(memberService.checkDuplicateNickname(nickname));
    }

    //무한스크롤
    @GetMapping("/members/infinite")
    public ResponseDto<?> getinfiniteMembers(@PageableDefault(size = 10) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return memberService.getInfiniteMembers(pageable);
    }
}
