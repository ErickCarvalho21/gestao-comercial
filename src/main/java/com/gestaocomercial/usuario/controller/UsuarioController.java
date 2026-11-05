package com.gestaocomercial.usuario.controller;

import com.gestaocomercial.usuario.dto.in.LoginDTORequest;
import com.gestaocomercial.usuario.dto.in.UsuarioDTORequest;
import com.gestaocomercial.usuario.dto.out.LoginDTOResponse;
import com.gestaocomercial.usuario.dto.out.UsuarioDTOResponse;
import com.gestaocomercial.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTOResponse> cadastrar(@RequestBody @Valid UsuarioDTORequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrarUsuario(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDTOResponse> login(@RequestBody @Valid LoginDTORequest request) {
        return ResponseEntity.ok(usuarioService.login(request));
    }
}
