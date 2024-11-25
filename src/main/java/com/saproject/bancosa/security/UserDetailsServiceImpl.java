package com.saproject.bancosa.security;

import com.saproject.bancosa.model.Usuario;
import com.saproject.bancosa.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findByEmailOrCpf(username, username);
        if (usuario.isPresent()) {
            return new UserDetailsImpl(usuario.get());
        } else {
            throw new UsernameNotFoundException("Usuário não encontrado com o username: " + username);
        }
    }
}
