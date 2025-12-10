package br.jus.trf1.sipe.lotacao;

import br.jus.trf1.sipe.lotacao.aplication.jsarh.LotacaoJSarh;
import br.jus.trf1.sipe.lotacao.infrastructure.persistence.LotacaoJpa;
import br.jus.trf1.sipe.lotacao.aplication.jsarh.dto.LotacaoJSarhResponse;

public class LotacaoMapping {

    private LotacaoMapping() {
    }


    public static LotacaoJSarh toJSarhDomain(LotacaoJSarhResponse lotacaoJSarhResponse) {
        return LotacaoJSarh.builder()
                .id(lotacaoJSarhResponse.getId())
                .sigla(lotacaoJSarhResponse.getSigla())
                .descricao(lotacaoJSarhResponse.getDescricao())
                .lotacaoPai(lotacaoJSarhResponse.getLotacaoPai() == null ? null : LotacaoJSarh.builder()
                        .id(lotacaoJSarhResponse.getLotacaoPai())
                        .build())
                .build();
    }

    public static LotacaoJpa toEntity(LotacaoJSarh lotacaoJSarh) {
        return LotacaoJpa.builder()
                .id(lotacaoJSarh.id())
                .sigla(lotacaoJSarh.sigla())
                .descricao(lotacaoJSarh.descricao())
                .lotacaoPai(lotacaoJSarh.lotacaoPai() == null ? null : LotacaoJpa.builder()
                        .id(lotacaoJSarh.lotacaoPai().id())
                        .build())
                .build();
    }
}
