package com.jiyoung.kikihi.global.auth.jwt.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.List;

@Getter
public class JWTUserDetails extends User {

    private final Long id;

    public JWTUserDetails(Long id, String email, List<GrantedAuthority> authorities) {
        super(email, "", authorities);
        this.id = id;
    }

    public static JWTUserDetails of(Long id, String email, String role) {
        return new JWTUserDetails(id, email, Collections.singletonList(new SimpleGrantedAuthority(role)));
    }
}
