package com.springSecurity.springSecurity.controller;

import com.springSecurity.springSecurity.config.JwtUtil;
import com.springSecurity.springSecurity.config.UserInfoUserDetailsService;
import com.springSecurity.springSecurity.model.AuthenticationRequest;
import com.springSecurity.springSecurity.model.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private UserInfoUserDetailsService userDetailsService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            // Tenta autenticar o usuário
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            // Se as credenciais estiverem incorretas, lança uma exceção com status 401
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nome de usuário ou senha incorretos", e);
        }

        // Se a autenticação for bem-sucedida, busca os detalhes do usuário
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        // Gera o token JWT
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        // Retorna o token na resposta
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}