package br.jus.trf1.sipe.arquivo.application.web.dto;

import lombok.Builder;

@Builder
public record ArquivoMetadataResponse(String id, String nome, String tipoDeConteudo, Long tamanho, String descricao) {
}
