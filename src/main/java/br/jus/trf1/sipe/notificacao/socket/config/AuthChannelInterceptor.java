package br.jus.trf1.sipe.notificacao.socket.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthChannelInterceptor implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                try {
                    // Validar JWT usando o decoder configurado pelo Spring Security
                    Jwt jwt = jwtDecoder.decode(token);

                    // Converter JWT em Authentication
                    Authentication authentication = jwtAuthenticationConverter.convert(jwt);

                    if (authentication != null && authentication.isAuthenticated()) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        accessor.setUser(authentication);

                        log.info("Usuário autenticado via WebSocket: {}", authentication.getName());
                    } else {
                        log.warn("Autenticação falhou para o token");
                        throw new RuntimeException("Token inválido");
                    }
                } catch (JwtValidationException e) {
                    log.warn("Token JWT inválido: {}", e.getMessage());
                    throw new RuntimeException("Token inválido");
                } catch (Exception e) {
                    log.error("Falha na autenticação WebSocket: {}", e.getMessage());
                    throw new RuntimeException("Falha na autenticação", e);
                }
            } else {
                log.warn("Conexão WebSocket sem token de autenticação");
                throw new RuntimeException("Token não fornecido");
            }
        }

        if (accessor != null && StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            validateSubscription(accessor);
        }

        return message;
    }

    private void validateSubscription(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();

        if (destination != null && destination.startsWith("/topic/notifications/")) {
            String[] parts = destination.split("/");
            if (parts.length >= 4) {
                String destinationUserId = parts[3];
                String authenticatedUserId = getAuthenticatedUserId(accessor);

                if (authenticatedUserId == null || !authenticatedUserId.equals(destinationUserId)) {
                    log.warn("Tentativa de acesso não autorizado ao tópico: {} por usuário: {}",
                            destination, authenticatedUserId);
                    throw new RuntimeException("Acesso não autorizado ao tópico");
                }

                log.debug("Subscription autorizada para usuário: {} no tópico: {}",
                        authenticatedUserId, destination);
            }
        }
    }

    private String getAuthenticatedUserId(StompHeaderAccessor accessor) {
        if (accessor.getUser() != null) {
            Authentication authentication = (Authentication) accessor.getUser();
            return jwtTokenUtil.getUsernameFromAuthentication(authentication);
        }
        return null;
    }
}