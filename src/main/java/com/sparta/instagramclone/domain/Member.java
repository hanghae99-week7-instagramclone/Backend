package com.sparta.instagramclone.domain;

import com.sparta.instagramclone.dto.request.ProfileRequestDto;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String profileUrl;

    @Column
    private String bio;

    @Column
    private String websiteUrl;

    public void updateProfile(ProfileRequestDto profileRequestDto, String profileUrl) {
        if (profileRequestDto.getUsername() != null) {
            this.username = profileRequestDto.getUsername();
        }
        if (profileRequestDto.getNickname() != null) {
            this.nickname = profileRequestDto.getNickname();
        }
        this.profileUrl = profileUrl;
        if (profileRequestDto.getWebsiteUrl() != null) {
            this.websiteUrl = profileRequestDto.getWebsiteUrl();
        }
        if (profileRequestDto.getBio() != null) {
            this.bio = profileRequestDto.getBio();
        }
    }

}
