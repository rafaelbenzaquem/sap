package br.jus.trf1.sipe.arquivo.infrastructure.jpa;

import br.jus.trf1.sipe.arquivo.domain.model.Arquivo;

public class ArquivoJpaMapper {
    public static ArquivoJpa toEntity(Arquivo arquivo) {
        return ArquivoJpa.builder()
                .id(arquivo.getId())
                .nome(arquivo.getNome())
                .tipoDeConteudo(arquivo.getTipoDeConteudo())
                .bytes(arquivo.getBytes())
                .descricao(arquivo.getDescricao())
                .build();
    }

    public static Arquivo toDomain(ArquivoJpa arquivoJpa) {
        return Arquivo.builder()
                .id(arquivoJpa.getId())
                .nome(arquivoJpa.getNome())
                .tipoDeConteudo(arquivoJpa.getTipoDeConteudo())
                .bytes(arquivoJpa.getBytes())
                .descricao(arquivoJpa.getDescricao())
                .build();
    }
}
