package com.sparta.instagramclone.domain;

import com.sparta.instagramclone.dto.request.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 다중 이미지
    @Column(columnDefinition = "mediumblob")
    @ElementCollection
    private List<String> imgUrlList;

    private String content;

    // 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void update(PostRequestDto postRequestDto, List<String> imgUrl) {
        this.imgUrlList = imgUrl;
        this.content = postRequestDto.getContent();
    }

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }
}
