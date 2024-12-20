package com.saproject.accountbank.dto;

import com.saproject.accountbank.model.User;

public record LoginResponseDTO(String token, User user) {
}
