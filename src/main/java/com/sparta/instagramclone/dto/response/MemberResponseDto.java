package com.sparta.instagramclone.dto.response;

import com.sparta.instagramclone.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String username;
    private String profileUrl;
    private Boolean followByMe;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.username = member.getUsername();
        this.profileUrl = member.getProfileUrl();
        this.createdAt = member.getCreatedAt();
        this.modifiedAt = member.getModifiedAt();
    }
}
