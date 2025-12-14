package br.jus.trf1.sipe.lotacao.application.jsarh;

import br.jus.trf1.sipe.lotacao.application.jsarh.dto.LotacaoJSarhResponse;
import br.jus.trf1.sipe.lotacao.domain.model.Lotacao;

public class LotacaoJSarhMapper {

    private LotacaoJSarhMapper() {
    }


    public static LotacaoJSarh toJSarhModel(LotacaoJSarhResponse lotacaoJSarhResponse) {
        return LotacaoJSarh.builder()
                .id(lotacaoJSarhResponse.getId())
                .sigla(lotacaoJSarhResponse.getSigla())
                .descricao(lotacaoJSarhResponse.getDescricao())
                .lotacaoPai(lotacaoJSarhResponse.getLotacaoPai() == null ? null : LotacaoJSarh.builder()
                        .id(lotacaoJSarhResponse.getLotacaoPai())
                        .build())
                .build();
    }

    public static LotacaoJSarh toJSarhModel(Lotacao lotacao) {
        return LotacaoJSarh.builder()
                .id(lotacao.getId())
                .sigla(lotacao.getSigla())
                .descricao(lotacao.getDescricao())
                .lotacaoPai(lotacao.getLotacaoPai() == null ? null : LotacaoJSarh.builder()
                        .id(lotacao.getLotacaoPai().getId())
                        .build())
                .build();
    }

    public static Lotacao toDomain(LotacaoJSarh lotacaoJSarh) {
        return Lotacao.builder()
                .id(lotacaoJSarh.id())
                .sigla(lotacaoJSarh.sigla())
                .descricao(lotacaoJSarh.descricao())
                .lotacaoPai(lotacaoJSarh.lotacaoPai() == null ? null : Lotacao.builder()
                        .id(lotacaoJSarh.lotacaoPai().id())
                        .build())
                .build();
    }
}
