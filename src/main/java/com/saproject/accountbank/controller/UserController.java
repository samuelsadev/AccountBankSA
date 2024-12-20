package com.saproject.accountbank.controller;

import com.saproject.accountbank.dto.LoginResponseDTO;;
import com.saproject.accountbank.dto.UserDTO;
import com.saproject.accountbank.model.Account;
import com.saproject.accountbank.model.User;
import com.saproject.accountbank.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User visualizarUsuario(@PathVariable Long id) {
        return userService.searchById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User criarUsuario(@Valid @RequestBody User user, @RequestParam(required = false) String accountType) {
        return userService.creatUserWithAccount(user,
                accountType != null ? Account.accountType.valueOf(accountType.toUpperCase()) : Account.accountType.CURRENT);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserDTO userDTO) {
        LoginResponseDTO response = userService.authenticateUser(userDTO);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public User atualizarUsuario(@PathVariable Long id, @Valid @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }
}
