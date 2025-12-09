package br.jus.trf1.sipe.servidor.infrastructure.persistence;

import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import br.jus.trf1.sipe.servidor.domain.port.out.ServidorRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ServidorRepositoryAdapter implements ServidorRepositoryPort {

    private final ServidorJpaRepository repository;

    @Override
    public Optional<Servidor> findByMatricula(String matricula) {
        return repository.findByMatricula(matricula);
    }

    @Override
    public List<Servidor> listarTodos() {
        return repository.listarTodos();
    }

    @Override
    public List<Servidor> listarPorLotacoes(Set<Integer> idsLotacoes) {
        return repository.listarPorLotacoes(idsLotacoes);
    }

    @Override
    public List<Servidor> listarPorNomeOuCrachaOuMatricula(String nome, Integer cracha, String matricula) {
        return repository.listarPorNomeOuCrachaOuMatricula(nome, cracha, matricula);
    }

    @Override
    public List<Servidor> listarPorNomeOuCrachaOuMatriculaEeLotacoes(String nome, Integer cracha, String matricula, Set<Integer> idsLotacoes) {
        return repository.listarPorNomeOuCrachaOuMatriculaEeLotacoes(nome, cracha, matricula, idsLotacoes);
    }

    @Override
    public Page<Servidor> paginarPorLotacoes(Set<Integer> idsLotacoes, Pageable pageable) {
        return repository.paginarPorLotacoes(idsLotacoes, pageable);
    }

    @Override
    public Page<Servidor> paginarPorNomeOuCrachaOuMatricula(String nome, Integer cracha, String matricula, Pageable pageable) {
        return repository.paginarPorNomeOuCrachaOuMatricula(nome, cracha, matricula, pageable);
    }

    @Override
    public Page<Servidor> paginarPorNomeOuCrachaOuMatriculaEeIdLotacao(String nome, Integer cracha, String matricula, Integer idLotacao, Pageable pageable) {
        return repository.paginarPorNomeOuCrachaOuMatriculaEeIdLotacao(nome, cracha, matricula, idLotacao, pageable);
    }

    @Override
    public Optional<Servidor> buscaDiretorLotacao(Integer idLotacao) {
        return repository.buscaDiretorLotacao(idLotacao);
    }

    @Override
    public Servidor save(Servidor servidor) {
        return repository.save(servidor);
    }

    @Override
    public Optional<Servidor> findById(Integer id) {
        return repository.findById(id);
    }
}