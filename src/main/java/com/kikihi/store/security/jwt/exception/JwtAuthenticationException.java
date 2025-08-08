package com.kikihi.store.security.jwt.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(String s) {
        super(s);
    }
}
