package br.jus.trf1.sipe.alteracao.alteracao_registro.infrastructure.jpa;

import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.AlteracaoRegistro;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.infrastructure.jpa.PedidoAlteracaoJpaMapper;
import br.jus.trf1.sipe.registro.infrastructure.jpa.RegistroJpaMapper;

public class AlteracaoRegistroJpaMapper {
    public static AlteracaoRegistroJpa toEntity(AlteracaoRegistro alteracaoRegistro) {
            return AlteracaoRegistroJpa.builder()
                    .id(alteracaoRegistro.getId())
                    .acao(alteracaoRegistro.getAcao())
                    .peidoAlteracao(PedidoAlteracaoJpaMapper.toEntity(alteracaoRegistro.getPeidoAlteracao()))
                    .registroOriginal(RegistroJpaMapper.toEntity(alteracaoRegistro.getRegistroOriginal()))
                    .registroNovo(RegistroJpaMapper.toEntity(alteracaoRegistro.getRegistroNovo()))
                    .build();
    }

    public static AlteracaoRegistro toDomain(AlteracaoRegistroJpa alteracaoRegistroJpa) {
        return AlteracaoRegistro.builder()
                .id(alteracaoRegistroJpa.getId())
                .acao(alteracaoRegistroJpa.getAcao())
                .peidoAlteracao(PedidoAlteracaoJpaMapper.toDomain(alteracaoRegistroJpa.getPeidoAlteracao()))
                .registroOriginal(RegistroJpaMapper.toDomain(alteracaoRegistroJpa.getRegistroOriginal()))
                .registroNovo(RegistroJpaMapper.toDomain(alteracaoRegistroJpa.getRegistroNovo()))
                .build();
    }
}
