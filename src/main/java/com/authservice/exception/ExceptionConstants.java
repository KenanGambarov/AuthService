package com.authservice.exception;

import lombok.Getter;

@Getter
public enum ExceptionConstants {

    VALIDATION_FAILED("Validation Failed"),
    UNEXPECTED_ERROR("Unexpected Error"),
    CLIENT_ERROR("Exception from Client"),
    ALREADY_EXIST("%s already exists"),
    USER_NOT_FOUND("User Not Found"),
    TOKEN_NOT_FOUND("Token Not Found"),
    WRONG_PASSWORD("Old password is wrong"),
    DEFAULT_ROLE_NOT_FOUND("Default role %s not found"),
    BAD_CREDENTIALS("Invalid username or password"),
    INVALID_TOKEN("Invalid Token");

    private final String message;

    ExceptionConstants(String message) {
        this.message = message;
    }

    public String getMessagePattern(Object... args) {
        return String.format(this.message, args);
    }
}
