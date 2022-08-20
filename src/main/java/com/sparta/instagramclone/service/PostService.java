package com.sparta.instagramclone.service;

import com.sparta.instagramclone.domain.Member;
import com.sparta.instagramclone.domain.Post;
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
    public ResponseDto<?> updatePost(Long postId, List<MultipartFile> multipartFile, PostRequestDto postRequestDto, HttpServletRequest request) throws IOException {
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = checkPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        List<String> imgUrlList = new ArrayList<>();

        if (multipartFile != null) {
            for (MultipartFile imgFile : multipartFile) {
                String imgUrl = awsS3Service.upload(imgFile);
                //String imgUrl = URLDecoder.decode(fileName, "UTF-8");
                if (imgUrl.equals("false")) {
                    return ResponseDto.fail("NOT_IMAGE_FILE", "이미지 파일만 업로드 가능합니다.");
                }
                imgUrlList.add(imgUrl);
                post.update(postRequestDto, imgUrlList);
            }
        } else {
            post.update(postRequestDto, null);
        }
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
    public ResponseDto<?> deletePost(Long postId, HttpServletRequest request) {
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = checkPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        List<String> deleteImgList = post.getImgUrlList();
        for (String img : deleteImgList) {
            log.info(img);
            String key = img.substring("https://mini-spring-bucket-team7.s3.ap-northeast-2.amazonaws.com/".length());
            awsS3Service.deleteS3(key);
        }

        postRepository.delete(post);
        return ResponseDto.success("delete success");
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (null == request.getHeader("Authorization")) {
            throw new MemberNotFoundException();
        }
        return jwtTokenProvider.getMemberFromAuthentication();
    }

    @Transactional(readOnly = true)
    public Post checkPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }


}
