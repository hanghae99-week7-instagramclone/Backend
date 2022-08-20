package com.sparta.instagramclone.jwt;

import com.sparta.instagramclone.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final String SECRET_KEY;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public void configure(HttpSecurity httpSecurity) {
        JwtAuthenticationFilter customFilter = new JwtAuthenticationFilter(SECRET_KEY, jwtTokenProvider, userDetailsService);
        httpSecurity.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

}