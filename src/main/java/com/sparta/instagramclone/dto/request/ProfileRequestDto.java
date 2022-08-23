package com.sparta.instagramclone.dto.request;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
public class ProfileRequestDto {
    private String username;
    private String nickname;
    private String websiteUrl;
    private String bio;
}
