package br.jus.trf1.sipe.notificacao;

import br.jus.trf1.sipe.usuario.Usuario;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public Page<Notificacao> buscarNotificacoesPorUsuario(Integer usuarioId, Boolean naoLidas, Pageable pageable) {
        if (naoLidas != null && naoLidas) {
            return notificacaoRepository.findByUsuarioIdAndFoiLidaFalse(usuarioId, pageable);
        }
        return notificacaoRepository.findByUsuarioId(usuarioId, pageable);
    }

    @Transactional
    public Notificacao criarNotificacao(Usuario usuario, String titulo, String mensagem,
                                        TipoNotificacao tipo, Map<String, Object> metadata) {

        String metadataJson = null;
        if (metadata != null && !metadata.isEmpty()) {
            try {
                metadataJson = objectMapper.writeValueAsString(metadata);
            } catch (JsonProcessingException e) {
                log.warn("Falha ao serializar metadata da notificação", e);
            }
        }

        Notificacao notificacao = Notificacao.builder()
                .usuario(usuario)
                .titulo(titulo)
                .mensagem(mensagem)
                .tipo(tipo)
                .foiLida(false)
                .createdAt(LocalDateTime.now())
                .metadata(metadataJson)
                .build();

        Notificacao savedNotificacao = notificacaoRepository.save(notificacao);

        // Enviar notificação via WebSocket
        enviarNotificacaoWebSocket(savedNotificacao);

        return savedNotificacao;
    }

    @Transactional
    public Notificacao marcarComoLida(Integer notificacaoId) {
        Notificacao notificacao = notificacaoRepository.findById(notificacaoId)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));

        notificacao.setFoiLida(true);
        return notificacaoRepository.save(notificacao);
    }

    @Transactional
    public void marcarTodasComoLidas(Integer usuarioId) {
        notificacaoRepository.marcarTodasComoLidas(usuarioId);
    }

    public Long contarNotificacoesNaoLidas(Integer usuarioId) {
        return notificacaoRepository.countByUsuarioIdAndFoiLidaFalse(usuarioId);
    }

    private void enviarNotificacaoWebSocket(Notificacao notificacao) {
        try {
            String destination = "/topic/notifications/" + notificacao.getUsuario().getId();
            messagingTemplate.convertAndSend(destination, notificacao);
            log.debug("Notificação enviada via WebSocket para o usuário: {}", notificacao.getUsuario().getId());
        } catch (Exception e) {
            log.error("Erro ao enviar notificação via WebSocket", e);
        }
    }
}