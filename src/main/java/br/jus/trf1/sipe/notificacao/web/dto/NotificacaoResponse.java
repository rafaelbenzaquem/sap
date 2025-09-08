package br.jus.trf1.sipe.notificacao.web.dto;

import br.jus.trf1.sipe.notificacao.TipoNotificacao;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificacaoResponse {
    private Integer id;
    private String titulo;
    private String mensagem;
    private TipoNotificacao tipo;
    private Boolean foiLida;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private Map<String, Object> metadata;
    private Integer usuarioId;
}