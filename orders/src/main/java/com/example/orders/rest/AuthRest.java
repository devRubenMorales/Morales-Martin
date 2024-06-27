package com.example.orders.rest;

import com.example.orders.security.AuthenticationReq;
import com.example.orders.security.TokenInfo;
import com.example.orders.security.JwtUtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;

@RestController
@RequestMapping
public class AuthRest {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    private static final Logger logger = LoggerFactory.getLogger(AuthRest.class);

    @PostMapping("/authenticate")
    public ResponseEntity<TokenInfo> authenticate(@RequestBody AuthenticationReq authenticationReq) {
        logger.info("Autenticando al usuario {}", authenticationReq.getUsername());

        try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationReq.getUsername(), authenticationReq.getPassword()));
        } catch (Exception e) {
            logger.error("Error al autenticar al usuario {}", authenticationReq.getUsername());
            return ResponseEntity.badRequest().build();

        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationReq.getUsername());
        final String jwt = jwtUtilService.generateToken(userDetails);

        return ResponseEntity.ok(new TokenInfo(jwt));
    }
}
