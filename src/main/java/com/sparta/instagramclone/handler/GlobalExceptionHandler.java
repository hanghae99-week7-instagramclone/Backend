package com.sparta.instagramclone.handler;

import com.sparta.instagramclone.handler.ex.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmailException() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.DUPLICATE_EMAIL.getCode(), ErrorCode.DUPLICATE_EMAIL.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateNicknameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateNicknameException() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.DUPLICATE_NICKNAME.getCode(), ErrorCode.DUPLICATE_NICKNAME.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmailNotFoundException() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.EMAIL_NOT_FOUND.getCode(), ErrorCode.EMAIL_NOT_FOUND.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordNotCollectException.class)
    public ResponseEntity<ErrorResponse> handlePasswordNotCollectException() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.PASSWORD_NOT_COLLECT.getCode(), ErrorCode.PASSWORD_NOT_COLLECT.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMemberNotFoundException() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.MEMBER_NOT_FOUND.getCode(), ErrorCode.MEMBER_NOT_FOUND.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProfileNotFoundExceptio(){
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.PROFILE_NOT_FOUND.getCode(), ErrorCode.PROFILE_NOT_FOUND.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundPostException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundPostException() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.NOT_FOUND_POST.getCode(), ErrorCode.NOT_FOUND_POST.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundCommentException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundCommentException() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.NOT_FOUND_COMMENT.getCode(), ErrorCode.NOT_FOUND_COMMENT.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotAuthorException.class)
    public ResponseEntity<ErrorResponse> handleNotAuthorException() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.NOT_AUTHOR.getCode(), ErrorCode.NOT_AUTHOR.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpiredException() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.TOKEN_EXPIRED.getCode(), ErrorCode.TOKEN_EXPIRED.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleApiRequestException(IllegalArgumentException ex) {
        Exception exception = new Exception();
        exception.setCode(HttpStatus.BAD_REQUEST);
        exception.setMessage(ex.getMessage());

        return new ResponseEntity(
                exception,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleApiRequestException(NullPointerException ex) {
        Exception exception = new Exception();
        exception.setCode(HttpStatus.BAD_REQUEST);
        exception.setMessage(ex.getMessage());

        return new ResponseEntity(
                exception,
                HttpStatus.BAD_REQUEST
        );
    }
}

