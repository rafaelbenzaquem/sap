package br.jus.trf1.sipe.arquivo.web.dto;

import br.jus.trf1.sipe.arquivo.db.Arquivo;
import lombok.Builder;

@Builder
public record ArquivoMetadataResponse(String id, String nome, String tipoDeConteudo, Long tamanho, String descricao) {

    public static ArquivoMetadataResponse of(Arquivo arquivo){
      return ArquivoMetadataResponse.builder()
              .id(arquivo.getId())
              .nome(arquivo.getNome())
              .tipoDeConteudo(arquivo.getTipoDeConteudo())
              .descricao(arquivo.getDescricao())
              .tamanho(arquivo.getBytes().length*8L)
              .build();
    }

}
