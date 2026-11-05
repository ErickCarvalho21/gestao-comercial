package com.gestaocomercial.usuario.security;

import com.gestaocomercial.usuario.entity.Usuario;
import com.gestaocomercial.usuario.enums.TipoUsuariosEnums;
import com.gestaocomercial.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminBootstrap implements ApplicationRunner {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.security.bootstrap-admin.name:}")
    private String name;
    @Value("${app.security.bootstrap-admin.email:}")
    private String email;
    @Value("${app.security.bootstrap-admin.password:}")
    private String password;

    @Override
    public void run(ApplicationArguments args) {
        boolean requested = !name.isBlank() || !email.isBlank() || !password.isBlank();
        if (!requested) {
            return;
        }
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            throw new IllegalStateException("Bootstrap ADMIN requer nome, email e senha.");
        }
        if (usuarioRepository.count() != 0) {
            throw new IllegalStateException("Bootstrap ADMIN permitido somente quando não há usuários.");
        }

        Usuario admin = Usuario.builder()
                .nome(name)
                .email(email)
                .senha(passwordEncoder.encode(password))
                .tipoUsuariosEnums(TipoUsuariosEnums.ADMIN)
                .build();
        usuarioRepository.save(admin);
    }
}
