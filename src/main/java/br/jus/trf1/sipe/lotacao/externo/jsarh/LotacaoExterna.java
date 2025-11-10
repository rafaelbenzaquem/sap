package br.jus.trf1.sipe.lotacao.externo.jsarh;

import br.jus.trf1.sipe.lotacao.externo.jsarh.dto.LotacaoExternaResponse;
import lombok.Builder;


@Builder
public record LotacaoExterna(Integer id, String sigla, String descricao, LotacaoExterna lotacaoPai) {

    public static LotacaoExterna from(LotacaoExternaResponse lotacaoExternaResponse) {
        return LotacaoExterna.builder()
                .id(lotacaoExternaResponse.getId())
                .sigla(lotacaoExternaResponse.getSigla())
                .descricao(lotacaoExternaResponse.getDescricao())
                .lotacaoPai(LotacaoExterna.builder()
                        .id(lotacaoExternaResponse.getLotacaoPai())
                        .build())
                .build();
    }
}
