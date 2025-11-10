package br.jus.trf1.sipe.lotacao;

import br.jus.trf1.sipe.lotacao.externo.jsarh.LotacaoExterna;

public class LotacaoMapping {

    private LotacaoMapping() {
    }

    public static Lotacao toModel(LotacaoExterna externa) {
        return Lotacao.builder()
                .id(externa.id())
                .sigla(externa.sigla())
                .descricao(externa.descricao())
                .lotacaoPai(externa.lotacaoPai() == null ? null : Lotacao.builder()
                        .id(externa.lotacaoPai().id())
                        .build())
                .build();
    }
}
