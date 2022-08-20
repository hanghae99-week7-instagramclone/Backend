package com.sparta.instagramclone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {

    private String email;

    private String nickname;

    private String username;

    private String password;

    private String passwordConfirm;

}
