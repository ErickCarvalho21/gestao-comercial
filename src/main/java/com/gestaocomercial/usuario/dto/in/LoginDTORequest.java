package com.gestaocomercial.usuario.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public record LoginDTORequest(@NotBlank @Email String email, @NotBlank String senha) {
}
