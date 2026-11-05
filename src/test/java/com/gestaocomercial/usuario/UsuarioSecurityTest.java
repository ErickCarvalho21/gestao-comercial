package com.gestaocomercial.usuario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestaocomercial.exception.EmailJaCadastradoException;
import com.gestaocomercial.usuario.converter.UsuarioConverter;
import com.gestaocomercial.usuario.dto.in.LoginDTORequest;
import com.gestaocomercial.usuario.dto.in.UsuarioDTORequest;
import com.gestaocomercial.usuario.dto.out.UsuarioDTOResponse;
import com.gestaocomercial.usuario.entity.Usuario;
import com.gestaocomercial.usuario.enums.TipoUsuariosEnums;
import com.gestaocomercial.usuario.repository.UsuarioRepository;
import com.gestaocomercial.usuario.security.AdminBootstrap;
import com.gestaocomercial.usuario.security.JwtRequestFilter;
import com.gestaocomercial.usuario.security.JwtUtil;
import com.gestaocomercial.usuario.security.SecurityErrorWriter;
import com.gestaocomercial.usuario.security.UserDetailsServiceImpl;
import com.gestaocomercial.usuario.service.UsuarioService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioSecurityTest {
    private static final byte[] KEY_BYTES = "0123456789abcdef0123456789abcdef".getBytes(StandardCharsets.UTF_8);
    private static final String KEY = Base64.getEncoder().encodeToString(KEY_BYTES);

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void cadastroCriptografaSenhaEResponseNaoContemSenha() throws Exception {
        UsuarioRepository repository = mock(UsuarioRepository.class);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UsuarioService service = new UsuarioService(repository, new UsuarioConverter(), encoder,
                mock(AuthenticationManager.class), new JwtUtil(KEY));
        when(repository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(1L);
            usuario.prePersist();
            return usuario;
        });

        UsuarioDTOResponse response = service.cadastrarUsuario(
                new UsuarioDTORequest("Ana", "ana@exemplo.com", "senha-segura", TipoUsuariosEnums.ADMIN));
        org.mockito.ArgumentCaptor<Usuario> captor = org.mockito.ArgumentCaptor.forClass(Usuario.class);
        verify(repository).save(captor.capture());
        assertNotEquals("senha-segura", captor.getValue().getSenha());
        assertTrue(encoder.matches("senha-segura", captor.getValue().getSenha()));
        assertFalse(new ObjectMapper().findAndRegisterModules().writeValueAsString(response).contains("senha"));
    }

    @Test
    void emailDuplicadoImpedeCadastro() {
        UsuarioRepository repository = mock(UsuarioRepository.class);
        when(repository.existsByEmail("ana@exemplo.com")).thenReturn(true);
        UsuarioService service = new UsuarioService(repository, new UsuarioConverter(),
                new BCryptPasswordEncoder(), mock(AuthenticationManager.class), new JwtUtil(KEY));
        assertThrows(EmailJaCadastradoException.class, () -> service.cadastrarUsuario(
                new UsuarioDTORequest("Ana", "ana@exemplo.com", "senha-segura", TipoUsuariosEnums.ADMIN)));
        verify(repository, never()).save(any());
    }

    @Test
    void loginValidoGeraTokenEInvalidoFalha() {
        AuthenticationManager manager = mock(AuthenticationManager.class);
        JwtUtil jwt = new JwtUtil(KEY);
        UsuarioService service = new UsuarioService(mock(UsuarioRepository.class), new UsuarioConverter(),
                new BCryptPasswordEncoder(), manager, jwt);
        String token = service.login(new LoginDTORequest("ana@exemplo.com", "correta")).token();
        assertEquals("ana@exemplo.com", jwt.extrairEmailToken(token));
        assertTrue(jwt.validateToken(token, "ana@exemplo.com"));

        when(manager.authenticate(any())).thenThrow(new BadCredentialsException("inválida"));
        assertThrows(BadCredentialsException.class,
                () -> service.login(new LoginDTORequest("ana@exemplo.com", "errada")));
    }

    @Test
    void usuarioRecebePerfilEDesativadoFicaDesabilitado() {
        UsuarioRepository repository = mock(UsuarioRepository.class);
        Usuario usuario = Usuario.builder().email("ana@exemplo.com").senha("hash")
                .tipoUsuariosEnums(TipoUsuariosEnums.FUNCIONARIO).ativo(true).build();
        when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        UserDetailsServiceImpl service = new UserDetailsServiceImpl(repository);
        UserDetails active = service.loadUserByUsername(usuario.getEmail());
        assertTrue(active.isEnabled());
        assertTrue(active.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_FUNCIONARIO")));

        usuario.setAtivo(false);
        assertFalse(service.loadUserByUsername(usuario.getEmail()).isEnabled());
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("outro@exemplo.com"));
    }

    @Test
    void filtroAceitaTokenValidoERejeitaInvalidoExpiradoEUsuarioInativo() throws Exception {
        UsuarioRepository repository = mock(UsuarioRepository.class);
        Usuario usuario = Usuario.builder().email("ana@exemplo.com").senha("hash")
                .tipoUsuariosEnums(TipoUsuariosEnums.ADMIN).ativo(true).build();
        when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        JwtUtil jwt = new JwtUtil(KEY);
        JwtRequestFilter filter = new JwtRequestFilter(jwt, new UserDetailsServiceImpl(repository),
                new SecurityErrorWriter(JsonMapper.builder().build()));

        MockHttpServletRequest validRequest = new MockHttpServletRequest("GET", "/produto");
        validRequest.addHeader("Authorization", "Bearer " + jwt.generateToken(usuario.getEmail()));
        MockHttpServletResponse validResponse = new MockHttpServletResponse();
        filter.doFilter(validRequest, validResponse, new MockFilterChain());
        assertEquals(200, validResponse.getStatus());
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());

        SecurityContextHolder.clearContext();
        assertUnauthorized(filter, "Bearer token.invalido");
        String expired = Jwts.builder().subject(usuario.getEmail())
                .issuedAt(Date.from(Instant.now().minusSeconds(7200)))
                .expiration(Date.from(Instant.now().minusSeconds(3600)))
                .signWith(Keys.hmacShaKeyFor(KEY_BYTES)).compact();
        assertUnauthorized(filter, "Bearer " + expired);
        assertUnauthorized(filter, "Bearer " + new JwtUtil(
                Base64.getEncoder().encodeToString("abcdef0123456789abcdef0123456789".getBytes(StandardCharsets.UTF_8)))
                .generateToken(usuario.getEmail()));
        usuario.setAtivo(false);
        assertUnauthorized(filter, "Bearer " + jwt.generateToken(usuario.getEmail()));
    }

    @Test
    void bootstrapSoCriaPrimeiroAdminComSenhaCriptografada() {
        UsuarioRepository repository = mock(UsuarioRepository.class);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        AdminBootstrap bootstrap = new AdminBootstrap(repository, encoder);
        ReflectionTestUtils.setField(bootstrap, "name", "Ana");
        ReflectionTestUtils.setField(bootstrap, "email", "ana@exemplo.com");
        ReflectionTestUtils.setField(bootstrap, "password", "senha-segura");
        bootstrap.run(null);
        org.mockito.ArgumentCaptor<Usuario> captor = org.mockito.ArgumentCaptor.forClass(Usuario.class);
        verify(repository).save(captor.capture());
        assertEquals(TipoUsuariosEnums.ADMIN, captor.getValue().getTipoUsuariosEnums());
        assertTrue(encoder.matches("senha-segura", captor.getValue().getSenha()));

        when(repository.count()).thenReturn(1L);
        assertThrows(IllegalStateException.class, () -> bootstrap.run(null));
    }

    private void assertUnauthorized(JwtRequestFilter filter, String authorization) throws Exception {
        SecurityContextHolder.clearContext();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/produto");
        request.addHeader("Authorization", authorization);
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, new MockFilterChain());
        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString().contains("Autenticação inválida"));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
