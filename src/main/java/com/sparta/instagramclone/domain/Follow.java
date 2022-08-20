package com.sparta.instagramclone.domain;

import com.sparta.instagramclone.dto.request.FollowRequestDto;
import com.sparta.instagramclone.dto.request.LikeRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "from_member_id")
    @ManyToOne
    private Member fromMember; // 구독 (팔로우) 하는 유저

    @JoinColumn(name = "to_member_id", nullable = false)
    @ManyToOne
    private Member toMember; // 구독 받는 유저

    @Builder
    public Follow(FollowRequestDto requestDto) {
        this.fromMember = requestDto.getFromMember();
        this.toMember = requestDto.getToMember();
    }
}
