package com.sparta.instagramclone.handler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class Exception
{
    private HttpStatus code;
    private String message;
}
