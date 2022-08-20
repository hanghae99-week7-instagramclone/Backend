package com.sparta.instagramclone.controller;

import com.sparta.instagramclone.dto.request.ProfileRequestDto;
import com.sparta.instagramclone.dto.response.ResponseDto;
import com.sparta.instagramclone.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.opengis.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;

    @PutMapping("")
    public ResponseDto<?> updateProfile(@RequestPart("data")ProfileRequestDto profileRequestDto, @RequestPart(value = "image", required = false) MultipartFile file, HttpServletRequest request) throws IOException {
        return profileService.updateProfile(profileRequestDto, file, request);
    }

    @GetMapping("/{memberId}")
    public ResponseDto<?> getProfile(@PathVariable Long memberId){
        return profileService.getProfile(memberId);
    }
}
