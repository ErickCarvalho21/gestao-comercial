package com.gestaocomercial.usuario.service;

import com.gestaocomercial.exception.EmailJaCadastradoException;
import com.gestaocomercial.usuario.converter.UsuarioConverter;
import com.gestaocomercial.usuario.dto.in.UsuarioDTORequest;
import com.gestaocomercial.usuario.dto.out.UsuarioDTOResponse;
import com.gestaocomercial.usuario.entity.Usuario;
import com.gestaocomercial.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gestaocomercial.usuario.dto.in.LoginDTORequest;
import com.gestaocomercial.usuario.dto.out.LoginDTOResponse;
import com.gestaocomercial.usuario.security.JwtUtil;

@Service
@RequiredArgsConstructor

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

     private void validarEmailDisponivel(String email){
         if(usuarioRepository.existsByEmail(email)){
             throw new EmailJaCadastradoException(
                     "Email ja existe"
             );
         }
     }

     @Transactional
     public UsuarioDTOResponse cadastrarUsuario(UsuarioDTORequest request){

         validarEmailDisponivel(request.getEmail());

         Usuario usuario = usuarioConverter.paraUsuario(request);
         usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
         Usuario usuarioSalvo = usuarioRepository.save(usuario);
         return usuarioConverter.paraUsuarioResponse(usuarioSalvo);
     }

    public LoginDTOResponse login(LoginDTORequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha()));
        return new LoginDTOResponse(jwtUtil.generateToken(request.email()));
    }

}
