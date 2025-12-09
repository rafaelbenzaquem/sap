package br.jus.trf1.sipe.lotacao.domain.service;

import br.jus.trf1.sipe.lotacao.domain.model.Lotacao;
import br.jus.trf1.sipe.lotacao.externo.jsarh.to.LotacaoExternaTO;

public class LotacaoMapping {

    private LotacaoMapping() {
    }

    public static Lotacao toModel(LotacaoExternaTO externa) {
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