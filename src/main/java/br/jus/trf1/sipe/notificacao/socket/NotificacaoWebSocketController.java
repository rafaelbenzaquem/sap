package br.jus.trf1.sipe.notificacao.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class NotificacaoWebSocketController {

    @SubscribeMapping("/topic/notifications/{usuarioId}")
    public void subscribeToNotifications() {
        // Subscription handling is automatic
    }

    @MessageMapping("/mark-read")
    @SendTo("/topic/notifications-update")
    public String handleMarkAsRead(String notificacaoId) {
        // Lógica para marcar como lida pode ser chamada aqui
        return "NOTIFICATION_READ:" + notificacaoId;
    }
}