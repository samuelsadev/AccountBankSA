package com.saproject.bancosa.security;

import com.saproject.bancosa.model.Conta;
import com.saproject.bancosa.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ContaRepository contaRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOuCpf) throws UsernameNotFoundException {
        Optional<Conta> conta = contaRepository.findByEmailOrCpf(emailOuCpf, emailOuCpf);

        if (conta.isPresent()) {
            return new UserDetailsImpl(conta.get());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Conta não encontrada.");
        }
    }
}
