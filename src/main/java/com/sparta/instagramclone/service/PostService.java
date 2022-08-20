package com.sparta.instagramclone.service;

import com.sparta.instagramclone.domain.Member;
import com.sparta.instagramclone.domain.Post;
import com.sparta.instagramclone.dto.JwtTokenDto;
import com.sparta.instagramclone.dto.request.PostRequestDto;
import com.sparta.instagramclone.dto.response.PostResponseDto;
import com.sparta.instagramclone.dto.response.ResponseDto;
import com.sparta.instagramclone.handler.ex.MemberNotFoundException;
import com.sparta.instagramclone.jwt.JwtTokenProvider;
import com.sparta.instagramclone.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final AwsS3Service awsS3Service;
    private final PostRepository postRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("cloud.aws.s3.bucket")
    private String bucket;

    @Transactional
    public ResponseDto<?> createPost(List<MultipartFile> multipartFile, PostRequestDto postRequestDto, HttpServletRequest request) throws IOException {
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        List<String> imgUrlList = new ArrayList<>();
        for (MultipartFile imgFile : multipartFile) {
            String fileName = awsS3Service.upload(imgFile);
            String imgUrl = URLDecoder.decode(fileName, "UTF-8");
            if (imgUrl.equals("false")) {
                return ResponseDto.fail("NOT_IMAGE_FILE", "이미지 파일만 업로드 가능합니다.");
            }
            imgUrlList.add(imgUrl);
        }

        Post post = Post.builder()
                .imgUrlList(imgUrlList)
                .content(postRequestDto.getContent())
                .member(member)
                .build();
        postRepository.save(post);

        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .author(post.getMember().getNickname())
                        .imgUrlList(post.getImgUrlList())
                        .content(post.getContent())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (null == request.getHeader("Authorization")) {
            throw new MemberNotFoundException();
        }
        return jwtTokenProvider.getMemberFromAuthentication();
    }

    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }


}
