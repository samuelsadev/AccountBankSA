package com.saproject.accountbank.service;

import com.saproject.accountbank.dto.LoginResponseDTO;
import com.saproject.accountbank.dto.UserDTO;
import com.saproject.accountbank.model.Account;
import com.saproject.accountbank.model.User;
import com.saproject.accountbank.repository.UserRepository;
import com.saproject.accountbank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ViaCepService viaCepService;

    public Optional<User> searchById(Long id) {
        return userRepository.findById(id);
    }

    public User creatUserWithAccount(User user, Account.accountType accountType) {

        if (user.getCep() != null && !user.getCep().isEmpty()) {
            ViaCepService.Endereco endereco = viaCepService.searchAddressByZipCode(user.getCep());
            if (endereco != null) {
                user.setAddress(endereco.getEnderecoCompleto());
            }
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User newUser = userRepository.save(user);

        Account account = new Account();
        account.setUser(newUser);
        account.setAccountType(accountType);
        account.setAccountNumber(generateUniqueNumber());
        account.setBalance(0.0);
        account.setActive(true);

        accountRepository.save(account);

        return newUser;
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setEmail(updatedUser.getEmail());

                    if (!updatedUser.getPassword().equals(user.getPassword())) {
                        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                    }

                    return userRepository.save(user);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }


    public LoginResponseDTO authenticateUser(UserDTO userDTO) {

        Optional<User> userOptional = userRepository.findByEmail(userDTO.emailOrCpf());

        if (!userOptional.isPresent()) {
            userOptional = userRepository.findByCpf(userDTO.emailOrCpf());
        }

        User user = userOptional
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email, CPF ou password inválidos"));

        if (!passwordEncoder.matches(userDTO.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email, CPF ou password inválidos");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponseDTO(token, user);
    }



    private String generateUniqueNumber() {
        return String.format("%08d", System.currentTimeMillis() % 100000000);
    }
}
