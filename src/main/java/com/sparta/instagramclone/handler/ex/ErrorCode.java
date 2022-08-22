package com.sparta.instagramclone.handler.ex;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    EMAIL_NOT_FOUND("U001", "존재하지 않은 EMAIL입니다."),
    DUPLICATE_EMAIL("U002", "중복된 사용자 EMAIL이 존재합니다."),
    DUPLICATE_NICKNAME("U003", "중복된 사용자 NICKNAME이 존재합니다."),
    PASSWORD_NOT_COLLECT("U004", "비밀번호가 일치하지 않습니다."),
    MEMBER_NOT_FOUND("U005", "존재하지 않은 사용자입니다."),
    PROFILE_NOT_FOUND("U006", "프로필 정보가 없습니다."),
    NOT_FOUND_POST("U007", "존재하지 않는 게시글입니다."),
    NOT_FOUND_COMMENT("U008", "존재하지 않는 댓글입니다."),
    NOT_AUTHOR("U009", "작성자가 아닙니다."),
    TOKEN_EXPIRED("U010", "만료된 토큰입니다.");

    private final String code;
    private final String message;

}
