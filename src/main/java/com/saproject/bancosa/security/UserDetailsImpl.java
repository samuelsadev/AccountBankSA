package com.saproject.bancosa.security;

import com.saproject.bancosa.model.Conta;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Conta conta;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return conta.getSenha();
    }

    @Override
    public String getUsername() {
        return conta.getEmail();
    }

    public String getCpf() {
        return conta.getCpf();
    }

    @Override
    public boolean isAccountNonExpired() {
        return conta.isAtivo();
    }

    @Override
    public boolean isAccountNonLocked() {
        return conta.isAtivo();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return conta.isAtivo();
    }

    @Override
    public boolean isEnabled() {
        return conta.isAtivo();
    }
}
