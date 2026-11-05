package com.gestaocomercial.usuario.converter;

import com.gestaocomercial.usuario.dto.in.UsuarioDTORequest;
import com.gestaocomercial.usuario.dto.out.UsuarioDTOResponse;
import com.gestaocomercial.usuario.entity.Usuario;
import org.springframework.stereotype.Component;

@Component

public class UsuarioConverter {

    public Usuario paraUsuario(UsuarioDTORequest usuariodto){
        return Usuario.builder()
                .email(usuariodto.getEmail())
                .nome(usuariodto.getNome())
                .senha(usuariodto.getSenha())
                .tipoUsuariosEnums(usuariodto.getTipoUsuariosEnums())
                .build();
    }

    public UsuarioDTOResponse paraUsuarioResponse(Usuario usuario){
        return UsuarioDTOResponse.builder()
                .id(usuario.getId())
                .ativo(usuario.getAtivo())
                .dataCadastro(usuario.getDataCadastro())
                .email(usuario.getEmail())
                .nome(usuario.getNome())
                .tipoUsuariosEnums(usuario.getTipoUsuariosEnums())
                .build();
    }


}
