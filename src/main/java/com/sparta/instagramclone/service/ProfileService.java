package com.sparta.instagramclone.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.sparta.instagramclone.domain.Member;
import com.sparta.instagramclone.dto.request.ProfileRequestDto;
import com.sparta.instagramclone.dto.response.MemberResponseDto;
import com.sparta.instagramclone.dto.response.ProfileResponseDto;
import com.sparta.instagramclone.dto.response.ResponseDto;
import com.sparta.instagramclone.jwt.JwtTokenProvider;
import com.sparta.instagramclone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AwsS3Service awsS3Service;
    private final AmazonS3Client amazonS3Client;
    private final MemberRepository memberRepository;

    @Value("cloud.aws.s3.bucket")
    private String bucket;

    @Transactional
    public ResponseDto<?> updateProfile(ProfileRequestDto profileRequestDto, MultipartFile file, HttpServletRequest request) throws IOException {
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        String profileUrl;
        if (file != null) {
            if (member.getProfileUrl() != null) {
                String key = member.getProfileUrl().substring("https://mini-spring-bucket-team7.s3.ap-northeast-2.amazonaws.com/".length());
                amazonS3Client.deleteObject(bucket, key);
            }
            profileUrl = awsS3Service.upload(file);
            member.updateProfile(profileRequestDto, profileUrl);
        } else {
            member.updateProfile(profileRequestDto, null);
        }
        return ResponseDto.success(ProfileResponseDto.builder()
                .bio(member.getBio())
                .createdAt(member.getCreatedAt())
                .id(member.getId())
                .email(member.getEmail())
                .modifiedAt(member.getModifiedAt())
                .profileUrl(member.getProfileUrl())
                .nickname(member.getNickname())
                .username(member.getUsername())
                .websiteUrl(member.getWebsiteUrl())
                .build());
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!jwtTokenProvider.validateToken(request.getHeader("Authorization").substring(7))) {
            return null;
        }
        return jwtTokenProvider.getMemberFromAuthentication();
    }

    @Transactional
    public ResponseDto<?> getProfile(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isPresent()) {
            return ResponseDto.success(ProfileResponseDto.builder()
                    .bio(member.get().getBio())
                    .createdAt(member.get().getCreatedAt())
                    .id(member.get().getId())
                    .email(member.get().getEmail())
                    .modifiedAt(member.get().getModifiedAt())
                    .profileUrl(member.get().getProfileUrl())
                    .nickname(member.get().getNickname())
                    .username(member.get().getUsername())
                    .websiteUrl(member.get().getWebsiteUrl())
                    .build());

        }
        return ResponseDto.fail("MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다.");
    }
}
