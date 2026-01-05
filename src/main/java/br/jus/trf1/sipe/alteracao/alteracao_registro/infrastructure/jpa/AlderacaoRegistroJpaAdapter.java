package br.jus.trf1.sipe.alteracao.alteracao_registro.infrastructure.jpa;

import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.Acao;
import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.AlteracaoRegistro;
import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.port.out.AlteracaoRegistroPersistencePort;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.PedidoAlteracao;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.infrastructure.jpa.PedidoAlteracaoJpaMapper;
import br.jus.trf1.sipe.registro.domain.model.Registro;
import br.jus.trf1.sipe.registro.infrastructure.jpa.RegistroJpa;
import br.jus.trf1.sipe.registro.infrastructure.jpa.RegistroJpaMapper;
import br.jus.trf1.sipe.registro.infrastructure.jpa.RegistroJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AlderacaoRegistroJpaAdapter implements AlteracaoRegistroPersistencePort {


    private final AlteracaoRegistroJpaRepository alteracaoRegistroJpaRepository;
    private final RegistroJpaRepository registroJpaRepository;

    public AlderacaoRegistroJpaAdapter(AlteracaoRegistroJpaRepository alteracaoRegistroJpaRepository, RegistroJpaRepository registroJpaRepository) {
        this.alteracaoRegistroJpaRepository = alteracaoRegistroJpaRepository;
        this.registroJpaRepository = registroJpaRepository;
    }

    @Override
    public AlteracaoRegistro salva(AlteracaoRegistro alteracaoRegistro) {
        var alteracaoRegistroJpa = AlteracaoRegistroJpaMapper.toEntity(alteracaoRegistro);
        alteracaoRegistroJpa = alteracaoRegistroJpaRepository.save(alteracaoRegistroJpa);
        return AlteracaoRegistroJpaMapper.toDomain(alteracaoRegistroJpa);
    }

    @Override
    public Optional<AlteracaoRegistro> buscaPorId(Long id) {
        return alteracaoRegistroJpaRepository.findById(id).map(AlteracaoRegistroJpaMapper::toDomain);
    }

    @Override
    public void apaga(AlteracaoRegistro alteracaoRegistro) {
        var alteracaoRegistroJpa = AlteracaoRegistroJpaMapper.toEntity(alteracaoRegistro);
        alteracaoRegistroJpaRepository.delete(alteracaoRegistroJpa);
    }

    @Transactional
    @Override
    public AlteracaoRegistro salvaAlteracaoDeRegistroDePonto(PedidoAlteracao pedidoAlteracao, Registro registroOriginal, Registro registroNovo, Acao acao) {
        var registroJpaNovo = RegistroJpaMapper.toEntity(registroNovo);
        registroJpaNovo = registroJpaRepository.save(registroJpaNovo);

        RegistroJpa registroJpaOriginal = null;
        if (registroOriginal != null) {
            registroJpaOriginal = RegistroJpaMapper.toEntity(registroOriginal);
            registroJpaOriginal.setRegistroNovo(registroJpaNovo);
            registroJpaOriginal = registroJpaRepository.save(registroJpaOriginal);
        }
        var alteracaoJpaRegistro = AlteracaoRegistroJpa.builder()
                .pedidoAlteracao(PedidoAlteracaoJpaMapper.toEntity(pedidoAlteracao))
                .registroOriginal(registroJpaOriginal)
                .registroNovo(registroJpaNovo)
                .acao(acao)
                .build();

        alteracaoJpaRegistro = alteracaoRegistroJpaRepository.save(alteracaoJpaRegistro);

        return AlteracaoRegistroJpaMapper.toDomain(alteracaoJpaRegistro);
    }
}
