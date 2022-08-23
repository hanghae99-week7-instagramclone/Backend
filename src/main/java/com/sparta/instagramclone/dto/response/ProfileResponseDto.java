package com.sparta.instagramclone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Builder
@Getter
@AllArgsConstructor
public class ProfileResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String username;
    private String profileUrl;
    private String bio;
    private String websiteUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
