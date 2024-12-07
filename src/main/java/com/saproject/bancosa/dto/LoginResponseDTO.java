package com.saproject.bancosa.dto;

import com.saproject.bancosa.model.Usuario;

public record LoginResponseDTO(String token, Usuario usuario) {
}
