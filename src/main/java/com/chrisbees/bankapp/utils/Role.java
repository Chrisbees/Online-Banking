package com.chrisbees.bankapp.utils;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.chrisbees.bankapp.utils.Permission.*;


@RequiredArgsConstructor
public enum Role {

    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    USER_READ,
                    USER_CREATE,
                    USER_DELETE,
                    USER_UPDATE
            )
    ),
    USER(
            Set.of(
                    USER_READ,
                    USER_CREATE,
                    USER_DELETE,
                    USER_UPDATE
            )
    );


    @Getter
    private final Set<Permission> permissions;


    public List<SimpleGrantedAuthority> grantedAuthorities() {
        var authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return authorities;
    }
}

