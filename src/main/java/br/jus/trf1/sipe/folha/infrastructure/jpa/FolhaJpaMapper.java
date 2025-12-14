package br.jus.trf1.sipe.folha.infrastructure.jpa;

import br.jus.trf1.sipe.folha.domain.model.Folha;
import br.jus.trf1.sipe.folha.domain.model.FolhaId;
import br.jus.trf1.sipe.servidor.infrastructure.jpa.ServidorJpaMapper;

public class FolhaJpaMapper {

    private FolhaJpaMapper() {
    }

    public static FolhaJpa toEntity(Folha folha) {
        return FolhaJpa.builder()
                .id(toEntityId(folha.getId()))
                .dataAbertura(folha.getDataAbertura())
                .servidorHomologador(ServidorJpaMapper.toEntity(folha.getServidorHomologador()))
                .dataHomologacao(folha.getDataHomologacao())
                .pontos(folha.getPontos())
                .build();
    }

    private static FolhaJpaId toEntityId(FolhaId folhaId) {
        return FolhaJpaId.builder()
                .ano(folhaId.getAno())
                .mes(folhaId.getMes().getValor())
                .servidor(ServidorJpaMapper.toEntity(folhaId.getServidor()))
                .build();
    }

    public static Folha toDomain(FolhaJpa folhaJpa) {
        return Folha.builder()
                .id(toDomainId(folhaJpa.getId()))
                .dataAbertura(folhaJpa.getDataAbertura())
                .servidorHomologador(ServidorJpaMapper.toDomain(folhaJpa.getServidorHomologador()))
                .dataHomologacao(folhaJpa.getDataHomologacao())
                .pontos(folhaJpa.getPontos())
                .build();
    }

    private static FolhaId toDomainId(FolhaJpaId folhaJpaId) {
        return FolhaId.builder()
                .ano(folhaJpaId.getAno())
                .mes(folhaJpaId.getMes().getValor())
                .servidor(ServidorJpaMapper.toDomain(folhaJpaId.getServidor()))
                .build();
    }
}
