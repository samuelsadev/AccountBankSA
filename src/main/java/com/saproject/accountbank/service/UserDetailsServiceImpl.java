package com.saproject.accountbank.service;

import com.saproject.accountbank.model.User;
import com.saproject.accountbank.repository.UserRepository;
import com.saproject.accountbank.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> usuario = usuarioRepository.findByEmail(username);

        if (!usuario.isPresent()) {
            usuario = usuarioRepository.findByCpf(username);
        }

        if (usuario.isPresent()) {
            return new UserDetailsImpl(usuario.get());
        } else {
            throw new UsernameNotFoundException("Usuário não encontrado com o username: " + username);
        }
    }
}
