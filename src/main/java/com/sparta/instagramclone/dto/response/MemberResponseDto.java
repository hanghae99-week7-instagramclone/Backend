package com.sparta.instagramclone.dto.response;

import com.sparta.instagramclone.domain.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.username = member.getUsername();
        this.createdAt = member.getCreatedAt();
        this.modifiedAt = member.getModifiedAt();
    }
}
