package com.sparta.instagramclone.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.instagramclone.domain.*;
import com.sparta.instagramclone.dto.response.PostInfiniteScrollResponseDto;
import com.sparta.instagramclone.dto.response.PostResponseDto;
import com.sparta.instagramclone.jwt.JwtTokenProvider;
import com.sparta.instagramclone.repository.LikeRepository;
import com.sparta.instagramclone.repository.PostRepositoryCustom;
import com.sparta.instagramclone.shared.Verification;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.Projection;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final LikeRepository likeRepository;
    private final JwtTokenProvider tokenProvider;
    @Override
    public Page<PostInfiniteScrollResponseDto> getPostPaging(Pageable pageable) {
        return null;
    }

    @Override
    public Slice<PostInfiniteScrollResponseDto> getPostScroll(Pageable pageable) {
        QPost post = QPost.post;
        Member member = tokenProvider.getMemberFromAuthentication();
        List<Post> results = queryFactory
                .select(post)
                .from(post)
                .orderBy(PostSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        List<PostInfiniteScrollResponseDto> content = new ArrayList<>();

        for (Post eachPost : results) {
            Optional<Like> likes = likeRepository.findByMemberAndPost(member, eachPost);
            boolean heartByMe;
            heartByMe = likes.isPresent();

            content.add(
                    PostInfiniteScrollResponseDto.builder()
                            .id(eachPost.getId())
                            .nickname(eachPost.getMember().getNickname())
                            .authorId(eachPost.getMember().getId())
                            .profileUrl(eachPost.getMember().getProfileUrl())
                            .content(eachPost.getContent())
                            .imgUrlList(eachPost.getImgUrlList())
                            .heartByMe(heartByMe)
                            .comments(eachPost.getComments())
                            .likes(eachPost.getLikes())
                            .createdAt(eachPost.getCreatedAt())
                            .modifiedAt(eachPost.getModifiedAt())
                            .build()
            );
        }
        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }

    private OrderSpecifier<?> PostSort(Pageable page) {
        QPost post = QPost.post;

        //서비스에서 보내준 Pageable 객체에 정렬조건 null 값 체크
        if (!page.getSort().isEmpty()) {
            //정렬값이 들어 있으면 for 사용하여 값을 가져온다
            for (Sort.Order order : page.getSort()) {
                // 서비스에서 넣어준 DESC or ASC 를 가져온다.
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                // 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 셋팅하여 준다.
                switch (order.getProperty()){
                    case "createdAt" :
                    case "descending":
                        return new OrderSpecifier(direction, post.createdAt);
                }
            }
        }
        return null;
    }

}
