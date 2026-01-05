package br.jus.trf1.sipe.alteracao.alteracao_registro.infrastructure.jpa;

import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.AlteracaoRegistro;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.infrastructure.jpa.PedidoAlteracaoJpaMapper;
import br.jus.trf1.sipe.registro.infrastructure.jpa.RegistroJpaMapper;

public class AlteracaoRegistroJpaMapper {

    private AlteracaoRegistroJpaMapper() {
    }

    public static AlteracaoRegistroJpa toEntity(AlteracaoRegistro alteracaoRegistro) {
        return AlteracaoRegistroJpa.builder()
                .id(alteracaoRegistro.getId())
                .acao(alteracaoRegistro.getAcao())
                .pedidoAlteracao(PedidoAlteracaoJpaMapper.toEntity(alteracaoRegistro.getPedidoAlteracao()))
                .registroOriginal((alteracaoRegistro.getRegistroOriginal() == null ? null
                        : RegistroJpaMapper.toEntity(alteracaoRegistro.getRegistroOriginal())))
                .registroNovo((alteracaoRegistro.getRegistroNovo() == null ? null
                        : RegistroJpaMapper.toEntity(alteracaoRegistro.getRegistroNovo())))
                .build();
    }

    public static AlteracaoRegistro toDomain(AlteracaoRegistroJpa alteracaoRegistroJpa) {
        return AlteracaoRegistro.builder()
                .id(alteracaoRegistroJpa.getId())
                .acao(alteracaoRegistroJpa.getAcao())
                .pedidoAlteracao(PedidoAlteracaoJpaMapper.toDomain(alteracaoRegistroJpa.getPedidoAlteracao()))
                .registroOriginal(alteracaoRegistroJpa.getRegistroOriginal() == null ? null
                        : RegistroJpaMapper.toDomain(alteracaoRegistroJpa.getRegistroOriginal()))
                .registroNovo(alteracaoRegistroJpa.getRegistroNovo() == null ? null
                        : RegistroJpaMapper.toDomain(alteracaoRegistroJpa.getRegistroNovo()))
                .build();
    }
}
