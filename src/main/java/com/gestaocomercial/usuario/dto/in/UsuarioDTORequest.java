package com.gestaocomercial.usuario.dto.in;


import com.gestaocomercial.usuario.enums.TipoUsuariosEnums;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UsuarioDTORequest {


    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private  String senha;


    @NotNull
    private TipoUsuariosEnums tipoUsuariosEnums;



}
