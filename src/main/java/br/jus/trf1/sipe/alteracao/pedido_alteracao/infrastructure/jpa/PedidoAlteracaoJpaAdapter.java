package br.jus.trf1.sipe.alteracao.pedido_alteracao.infrastructure.jpa;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.PedidoAlteracao;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.port.out.PedidoAlteracaoPersistencePort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PedidoAlteracaoJpaAdapter implements PedidoAlteracaoPersistencePort {

    private final PedidoAlteracaoJpaRepository repository;

    public PedidoAlteracaoJpaAdapter(PedidoAlteracaoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public PedidoAlteracao salva(PedidoAlteracao pedidoAlteracao) {
        var pedidoAlteracaoJpa = PedidoAlteracaoJpaMapper.toEntity(pedidoAlteracao);
        pedidoAlteracaoJpa = repository.save(pedidoAlteracaoJpa);
        return PedidoAlteracaoJpaMapper.toDomain(pedidoAlteracaoJpa);
    }

    @Override
    public Optional<PedidoAlteracao> buscaPorId(Long idPedidoAlteracao) {
        return Optional.empty();
    }

    @Override
    public Optional<PedidoAlteracao> buscaPorPonto(String matricula, LocalDate dia, boolean emAprovacao) {
        if (emAprovacao)
            return repository.buscaPorPontoEmAprovacao(matricula, dia).map(PedidoAlteracaoJpaMapper::toDomain);
        return repository.buscaPorPonto(matricula, dia).map(PedidoAlteracaoJpaMapper::toDomain);
    }

    @Override
    public void apagarPorId(long idPedidoAlteracao) {
        repository.deleteById(idPedidoAlteracao);
    }
}
