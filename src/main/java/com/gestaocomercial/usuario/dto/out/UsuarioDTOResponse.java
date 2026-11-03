package com.gestaocomercial.usuario.dto.out;

import com.gestaocomercial.usuario.enums.TipoUsuariosEnums;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTOResponse {


        private Long id;

        private String nome;

        private String email;

        private Boolean ativo;

        private TipoUsuariosEnums tipoUsuariosEnums;

        private LocalDateTime dataCadastro;


}
