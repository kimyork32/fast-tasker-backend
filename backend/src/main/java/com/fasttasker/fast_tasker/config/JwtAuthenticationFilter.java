package com.fasttasker.fast_tasker.config;
// creado con Gemini
// arreglado por chatgpt

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String token = null;

        // 1. Intentar leer Authorization: Bearer xxx
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        // 2. Si no hay Authorization, intentar por cookie
        if (token == null && request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if (cookie.getName().equals("jwtToken")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // 3. Si aun no tenemos token, continuar sin autenticar
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 4. Validar token JWT
            if (jwtService.isTokenValid(token)) {
                UUID accountId = jwtService.extractAccountId(token);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {

                    var authToken = new UsernamePasswordAuthenticationToken(
                            accountId,
                            null,
                            Collections.emptyList()
                    );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception ignored) {}

        filterChain.doFilter(request, response);
    }
}