package com.jiyoung.kikihi.security.jwt.domain;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class JWTUserDetails extends User {

    private final UUID id;

    public JWTUserDetails(UUID id, String email, List<GrantedAuthority> authorities) {
        super(email, "", authorities);
        this.id = id;
    }

    public static JWTUserDetails of(UUID id, String email, String role) {
        return new JWTUserDetails(id, email, Collections.singletonList(new SimpleGrantedAuthority(role)));
    }
}
