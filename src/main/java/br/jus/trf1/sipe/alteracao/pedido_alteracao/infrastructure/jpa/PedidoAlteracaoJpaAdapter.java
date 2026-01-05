package br.jus.trf1.sipe.alteracao.pedido_alteracao.infrastructure.jpa;

import br.jus.trf1.sipe.alteracao.alteracao_registro.infrastructure.jpa.AlteracaoRegistroJpaMapper;
import br.jus.trf1.sipe.alteracao.alteracao_registro.infrastructure.jpa.AlteracaoRegistroJpaRepository;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.PedidoAlteracao;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.port.out.PedidoAlteracaoPersistencePort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PedidoAlteracaoJpaAdapter implements PedidoAlteracaoPersistencePort {

    private final PedidoAlteracaoJpaRepository pedidoAlteracaoJpaRepository;
    private final AlteracaoRegistroJpaRepository alteracaoRegistroJpaRepository;


    public PedidoAlteracaoJpaAdapter(PedidoAlteracaoJpaRepository pedidoAlteracaoJpaRepository,
                                     AlteracaoRegistroJpaRepository alteracaoRegistroJpaRepository) {
        this.pedidoAlteracaoJpaRepository = pedidoAlteracaoJpaRepository;
        this.alteracaoRegistroJpaRepository = alteracaoRegistroJpaRepository;
    }

    @Override
    public PedidoAlteracao salva(PedidoAlteracao pedidoAlteracao) {
        var pedidoAlteracaoJpa = PedidoAlteracaoJpaMapper.toEntity(pedidoAlteracao);
        pedidoAlteracaoJpa = pedidoAlteracaoJpaRepository.save(pedidoAlteracaoJpa);
        return PedidoAlteracaoJpaMapper.toDomain(pedidoAlteracaoJpa);
    }

    @Override
    public Optional<PedidoAlteracao> buscaPorId(Long idPedidoAlteracao) {

        var pedidoAlteracaoJpaOpt = pedidoAlteracaoJpaRepository.buscaPorId(idPedidoAlteracao);
        return toOptionalDomain(pedidoAlteracaoJpaOpt);
    }

    @Override
    public Optional<PedidoAlteracao> buscaPorPonto(String matricula, LocalDate dia, boolean emAprovacao) {
        if (emAprovacao) {
            var pedidoAlteracaoJpaOpt = pedidoAlteracaoJpaRepository.buscaPorPontoEmAprovacao(matricula, dia);
            return toOptionalDomain(pedidoAlteracaoJpaOpt);
        }
        var pedidoAlteracaoJpaOpt = pedidoAlteracaoJpaRepository.buscaPorPonto(matricula, dia);
        return toOptionalDomain(pedidoAlteracaoJpaOpt);
    }

    @Override
    public void apagarPorId(long idPedidoAlteracao) {
        pedidoAlteracaoJpaRepository.deleteById(idPedidoAlteracao);
    }

    private Optional<PedidoAlteracao> toOptionalDomain(Optional<PedidoAlteracaoJpa> pedidoAlteracaoJpaOpt) {
        if (pedidoAlteracaoJpaOpt.isPresent()) {
        var pedidoAlteracaoOpt = pedidoAlteracaoJpaOpt.map(PedidoAlteracaoJpaMapper::toDomain);
            var alteracoesRegistros = alteracaoRegistroJpaRepository.listaPorPedidoAlteracao(pedidoAlteracaoJpaOpt.get().getId());
            return pedidoAlteracaoOpt.map(pa -> {
                pa.setAlteracoesRegistros(alteracoesRegistros.stream().map(AlteracaoRegistroJpaMapper::toDomain).toList());
                return pa;
            });
        }
        return Optional.empty();
    }
}
