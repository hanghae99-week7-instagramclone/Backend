package com.sparta.instagramclone.dto.request;

import com.sparta.instagramclone.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowRequestDto {
    private Member fromMember;
    private Member toMember;
}
