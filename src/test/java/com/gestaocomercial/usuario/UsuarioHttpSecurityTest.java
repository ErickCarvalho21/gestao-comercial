package com.gestaocomercial.usuario;

import com.gestaocomercial.exception.EmailJaCadastradoException;
import com.gestaocomercial.usuario.controller.UsuarioController;
import com.gestaocomercial.usuario.dto.out.LoginDTOResponse;
import com.gestaocomercial.usuario.dto.out.UsuarioDTOResponse;
import com.gestaocomercial.usuario.entity.Usuario;
import com.gestaocomercial.usuario.enums.TipoUsuariosEnums;
import com.gestaocomercial.usuario.repository.UsuarioRepository;
import com.gestaocomercial.usuario.security.JwtUtil;
import com.gestaocomercial.usuario.security.SecurityConfig;
import com.gestaocomercial.usuario.security.SecurityErrorWriter;
import com.gestaocomercial.usuario.security.UserDetailsServiceImpl;
import com.gestaocomercial.usuario.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@Import({SecurityConfig.class, JwtUtil.class, UserDetailsServiceImpl.class, SecurityErrorWriter.class})
@org.springframework.test.context.TestPropertySource(properties =
        "app.security.jwt-secret=MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=")
class UsuarioHttpSecurityTest {
    @Autowired MockMvc mvc;
    @Autowired JwtUtil jwtUtil;
    @Autowired AuthenticationManager authenticationManager;
    @Autowired PasswordEncoder passwordEncoder;
    @MockitoBean UsuarioService usuarioService;
    @MockitoBean UsuarioRepository usuarioRepository;

    private static final String CADASTRO = """
            {"nome":"Ana","email":"ana@exemplo.com","senha":"senha-segura","tipoUsuariosEnums":"ADMIN"}
            """;

    @Test
    void cadastroSemTokenRetorna401EFuncionarioRetorna403() throws Exception {
        mvc.perform(post("/usuario").contentType(MediaType.APPLICATION_JSON).content(CADASTRO))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));

        String token = tokenPara(TipoUsuariosEnums.FUNCIONARIO);
        mvc.perform(post("/usuario").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(CADASTRO))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void adminCadastraERecebeResponseSemSenha() throws Exception {
        String token = tokenPara(TipoUsuariosEnums.ADMIN);
        when(usuarioService.cadastrarUsuario(any())).thenReturn(UsuarioDTOResponse.builder()
                .id(1L).nome("Ana").email("ana@exemplo.com").ativo(true)
                .tipoUsuariosEnums(TipoUsuariosEnums.ADMIN).build());
        mvc.perform(post("/usuario").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(CADASTRO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("ana@exemplo.com"))
                .andExpect(jsonPath("$.senha").doesNotExist());
    }

    @Test
    void cadastroInvalidoRetorna400EEmailDuplicado409() throws Exception {
        String token = tokenPara(TipoUsuariosEnums.ADMIN);
        mvc.perform(post("/usuario").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());

        when(usuarioService.cadastrarUsuario(any())).thenThrow(new EmailJaCadastradoException("Email já existe"));
        mvc.perform(post("/usuario").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(CADASTRO))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void loginPublicoRetornaTokenOu401() throws Exception {
        when(usuarioService.login(any())).thenReturn(new LoginDTOResponse("token-jwt"));
        mvc.perform(post("/usuario/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"ana@exemplo.com\",\"senha\":\"correta\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-jwt"));

        when(usuarioService.login(any())).thenThrow(new BadCredentialsException("detalhe interno"));
        mvc.perform(post("/usuario/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"ana@exemplo.com\",\"senha\":\"errada\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Credenciais inválidas"));
    }

    @Test
    void authenticationManagerRejeitaUsuarioInativo() {
        Usuario inativo = Usuario.builder().email("inativo@exemplo.com")
                .senha(passwordEncoder.encode("senha-segura"))
                .tipoUsuariosEnums(TipoUsuariosEnums.FUNCIONARIO).ativo(false).build();
        when(usuarioRepository.findByEmail(inativo.getEmail())).thenReturn(Optional.of(inativo));
        assertThrows(AuthenticationException.class, () -> authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(inativo.getEmail(), "senha-segura")));

        Usuario ativo = Usuario.builder().email("ativo@exemplo.com")
                .senha(passwordEncoder.encode("senha-segura"))
                .tipoUsuariosEnums(TipoUsuariosEnums.FUNCIONARIO).ativo(true).build();
        when(usuarioRepository.findByEmail(ativo.getEmail())).thenReturn(Optional.of(ativo));
        assertTrue(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(ativo.getEmail(), "senha-segura")).isAuthenticated());
    }

    private String tokenPara(TipoUsuariosEnums perfil) {
        Usuario usuario = Usuario.builder().email("ana@exemplo.com").senha("hash")
                .tipoUsuariosEnums(perfil).ativo(true).build();
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        return jwtUtil.generateToken(usuario.getEmail());
    }
}
