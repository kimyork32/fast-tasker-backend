package com.fasttasker.fast_tasker.config;
// creado con Gemini

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

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final UUID accountId;

        // 1. Si no hay header o no empieza con "Bearer ", pasamos al siguiente filtro.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extraemos el token (quitando "Bearer ")
        jwt = authHeader.substring(7);

        try {
            // 3. Validamos el token y extraemos el ID
            if (jwtService.isTokenValid(jwt)) {
                accountId = jwtService.extractAccountId(jwt);

                // 4. ¡LA MAGIA! Si el token es válido y no hay nadie
                //    autenticado AÚN en esta petición...
                if (SecurityContextHolder.getContext().getAuthentication() == null) {

                    // Creamos un objeto de autenticación
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    accountId, // <-- Ponemos el UUID como el "Principal"
                                    null,
                                    Collections.emptyList() // Sin roles por ahora
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 5. Guardamos la autenticación en el Contexto de Seguridad.
                    // A partir de aquí, Spring sabe que el usuario ESTÁ autenticado.
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Si el token es inválido (expirado, firma mal), no hacemos nada.
            // La petición continuará sin autenticación y fallará
            // en la capa de SecurityConfig (si el endpoint no es público).
        }

        // 6. Pasamos al siguiente filtro de la cadena.
        filterChain.doFilter(request, response);
    }
}