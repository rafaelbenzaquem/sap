package br.jus.trf1.sipe.notificacao.socket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificacaoReadPayload {
    private Integer notificacaoId;
}

