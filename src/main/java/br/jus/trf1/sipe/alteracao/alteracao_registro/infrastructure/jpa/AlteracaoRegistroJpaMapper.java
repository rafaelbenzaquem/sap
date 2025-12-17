package br.jus.trf1.sipe.alteracao.alteracao_registro.infrastructure.jpa;

import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.AlteracaoRegistro;

public class AlteracaoRegistroJpaMapper {
    public static AlteracaoRegistroJpa toEntity(AlteracaoRegistro alteracaoRegistro) {
            return AlteracaoRegistroJpa.builder()
                    .id(alteracaoRegistro.getId())
                    .acao(alteracaoRegistro.getAcao())
                    .peidoAlteracao(alteracaoRegistro.getPeidoAlteracao())
                    .registroOriginal(alteracaoRegistro.getRegistroOriginal())
                    .registroNovo(alteracaoRegistro.getRegistroNovo())
                    .build();
    }

    public static AlteracaoRegistro toDomain(AlteracaoRegistroJpa alteracaoRegistroJpa) {
        return AlteracaoRegistro.builder()
                .id(alteracaoRegistroJpa.getId())
                .acao(alteracaoRegistroJpa.getAcao())
                .peidoAlteracao(alteracaoRegistroJpa.getPeidoAlteracao())
                .registroOriginal(alteracaoRegistroJpa.getRegistroOriginal())
                .registroNovo(alteracaoRegistroJpa.getRegistroNovo())
                .build();
    }
}
