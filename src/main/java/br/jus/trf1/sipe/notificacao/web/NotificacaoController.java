package br.jus.trf1.sipe.notificacao.web;

import br.jus.trf1.sipe.notificacao.Notificacao;
import br.jus.trf1.sipe.notificacao.NotificacaoService;
import br.jus.trf1.sipe.notificacao.web.dto.ContadorNotificacoesResponse;
import br.jus.trf1.sipe.notificacao.web.dto.NotificacaoRequest;
import br.jus.trf1.sipe.notificacao.web.dto.NotificacaoResponse;
import br.jus.trf1.sipe.usuario.Usuario;
import br.jus.trf1.sipe.usuario.UsuarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/sipe/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoService notificacaoService;
    private final UsuarioService usuarioService;
    private final ObjectMapper objectMapper;

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Page<NotificacaoResponse>> listarNotificacoes(
            @PathVariable Integer usuarioId,
            @RequestParam(required = false) Boolean naoLidas,
            Pageable pageable) {

        Page<Notificacao> notificacoes = notificacaoService.buscarNotificacoesPorUsuario(usuarioId, naoLidas, pageable);
        Page<NotificacaoResponse> response = notificacoes.map(this::toResponse);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<NotificacaoResponse> criarNotificacao(@Valid @RequestBody NotificacaoRequest request) {
        Usuario usuario = usuarioService.buscaPorId(request.getUsuarioId());

        Notificacao notificacao = notificacaoService.criarNotificacao(
                usuario,
                request.getTitulo(),
                request.getMensagem(),
                request.getTipo(),
                request.getMetadata());

        return ResponseEntity.ok(toResponse(notificacao));
    }

    @PatchMapping("/{notificacaoId}/marcar-lida")
    public ResponseEntity<NotificacaoResponse> marcarComoLida(@PathVariable Integer notificacaoId) {
        Notificacao notificacao = notificacaoService.marcarComoLida(notificacaoId);
        return ResponseEntity.ok(toResponse(notificacao));
    }

    @PatchMapping("/usuario/{usuarioId}/marcar-todas-lidas")
    public ResponseEntity<Void> marcarTodasComoLidas(@PathVariable Integer usuarioId) {
        notificacaoService.marcarTodasComoLidas(usuarioId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/usuario/{usuarioId}/contador")
    public ResponseEntity<ContadorNotificacoesResponse> contarNotificacoesNaoLidas(@PathVariable Integer usuarioId) {
        Long totalNaoLidas = notificacaoService.contarNotificacoesNaoLidas(usuarioId);
        return ResponseEntity.ok(ContadorNotificacoesResponse.builder().totalNaoLidas(totalNaoLidas).build());
    }

    private NotificacaoResponse toResponse(Notificacao notificacao) {
        Map<String, Object> metadata = null;
        if (notificacao.getMetadata() != null) {
            try {
                metadata = objectMapper.readValue(notificacao.getMetadata(), Map.class);
            } catch (JsonProcessingException e) {
                // Logar erro mas não falhar a conversão
            }
        }

        return NotificacaoResponse.builder()
                .id(notificacao.getId())
                .titulo(notificacao.getTitulo())
                .mensagem(notificacao.getMensagem())
                .tipo(notificacao.getTipo())
                .foiLida(notificacao.getFoiLida())
                .createdAt(notificacao.getCreatedAt())
                .metadata(metadata)
                .usuarioId(notificacao.getUsuario().getId())
                .build();
    }
}