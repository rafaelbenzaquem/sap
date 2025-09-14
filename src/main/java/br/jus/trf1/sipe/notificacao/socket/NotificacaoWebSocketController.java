package br.jus.trf1.sipe.notificacao.socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
 import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import br.jus.trf1.sipe.notificacao.NotificacaoService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NotificacaoWebSocketController {

    private final NotificacaoService notificacaoService;

    @SubscribeMapping("/topic/notifications/{usuarioId}")
    public void subscribeToNotifications() {

        // Subscription handling is automatic
    }

    @MessageMapping("/mark-read")
    @SendTo("/topic/notifications-update")
    public String handleMarkAsRead(@Payload NotificacaoReadPayload payload) {
        if (payload == null || payload.getNotificacaoId() == null) {
            log.warn("Payload inválido para marcar leitura via WS");
            return "INVALID_PAYLOAD";
        }

        try {
            notificacaoService.marcarComoLida(payload.getNotificacaoId());
            return "NOTIFICATION_READ:" + payload.getNotificacaoId();
        } catch (Exception e) {
            log.error("Erro ao marcar notificação como lida via WebSocket", e);
            return "ERROR:" + e.getMessage();
        }
    }
}
