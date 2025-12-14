package br.jus.trf1.sipe.lotacao.infrastructure.persistence;

import br.jus.trf1.sipe.lotacao.domain.model.Lotacao;

public class LotacaoJpaMapper {
    private LotacaoJpaMapper() {
    }

    public static LotacaoJpa toEntity(Lotacao lotacao) {
        return LotacaoJpa.builder()
                .id(lotacao.getId())
                .sigla(lotacao.getSigla())
                .descricao(lotacao.getDescricao())
                .lotacaoPai(lotacao.getLotacaoPai() == null ? null : LotacaoJpa.builder()
                        .id(lotacao.getLotacaoPai().getId())
                        .build())
                .build();
    }

    public static Lotacao toDomain(LotacaoJpa lotacaoJpa) {
        return Lotacao.builder()
                .id(lotacaoJpa.getId())
                .sigla(lotacaoJpa.getSigla())
                .descricao(lotacaoJpa.getDescricao())
                .lotacaoPai(lotacaoJpa.getLotacaoPai() == null ? null : Lotacao.builder()
                        .id(lotacaoJpa.getLotacaoPai().getId())
                        .build())
                .build();
    }

}
