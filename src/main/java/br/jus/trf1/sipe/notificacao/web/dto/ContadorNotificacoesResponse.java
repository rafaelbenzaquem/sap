package br.jus.trf1.sipe.notificacao.web.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContadorNotificacoesResponse {
    private Long totalNaoLidas;
}