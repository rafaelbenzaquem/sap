package br.jus.trf1.sipe.notificacao.web.dto;

import br.jus.trf1.sipe.notificacao.TipoNotificacao;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificacaoRequest {
    private String titulo;
    private String mensagem;
    private TipoNotificacao tipo;
    private Map<String, Object> metadata;
    private Integer usuarioId;
}