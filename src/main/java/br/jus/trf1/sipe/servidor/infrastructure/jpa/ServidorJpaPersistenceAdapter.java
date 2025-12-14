package br.jus.trf1.sipe.servidor.infrastructure.jpa;

import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import br.jus.trf1.sipe.servidor.domain.port.out.ServidorPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ServidorJpaPersistenceAdapter implements ServidorPersistencePort {

    private final ServidorJpaRepository repository;

    @Override
    public Optional<Servidor> buscaPorMatricula(String matricula) {
        return repository.findByMatricula(matricula).map(ServidorJpaMapper::toDomain);
    }

    @Override
    public List<Servidor> listarTodos() {
        return repository.listarTodos().stream().map(ServidorJpaMapper::toDomain).toList();
    }

    @Override
    public List<Servidor> listarPorLotacoes(Set<Integer> idsLotacoes) {
        return repository.listarPorLotacoes(idsLotacoes).stream().map(ServidorJpaMapper::toDomain).toList();
    }

    @Override
    public List<Servidor> listarPorNomeOuCrachaOuMatricula(String nome, Integer cracha, String matricula) {
        return repository.listarPorNomeOuCrachaOuMatricula(nome, cracha, matricula).stream().map(ServidorJpaMapper::toDomain).toList();
    }

    @Override
    public List<Servidor> listarPorNomeOuCrachaOuMatriculaEeLotacoes(String nome, Integer cracha, String matricula, Set<Integer> idsLotacoes) {
        return repository.listarPorNomeOuCrachaOuMatriculaEeLotacoes(nome, cracha, matricula, idsLotacoes).stream().map(ServidorJpaMapper::toDomain).toList();
    }

    @Override
    public List<Servidor> paginar(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).getContent().stream().map(ServidorJpaMapper::toDomain).toList();
    }

    @Override
    public List<Servidor> paginarPorLotacoes(Set<Integer> idsLotacoes, int page, int size) {
        return repository.paginarPorLotacoes(idsLotacoes, PageRequest.of(page, size)).getContent().stream().map(ServidorJpaMapper::toDomain).toList();
    }

    @Override
    public List<Servidor> paginarPorNomeOuCrachaOuMatricula(String nome, Integer cracha, String matricula, int page, int size) {
        return repository.paginarPorNomeOuCrachaOuMatricula(nome, cracha, matricula, PageRequest.of(page, size)).getContent().stream().map(ServidorJpaMapper::toDomain).toList();
    }

    @Override
    public List<Servidor> paginarPorNomeOuCrachaOuMatriculaEeIdLotacao(String nome, Integer cracha, String matricula, Integer idLotacao,  int page, int size) {
        return repository.paginarPorNomeOuCrachaOuMatriculaEeIdLotacao(nome, cracha, matricula, idLotacao, PageRequest.of(page, size)).getContent().stream().map(ServidorJpaMapper::toDomain).toList();
    }

    @Override
    public Optional<Servidor> buscaDiretorLotacao(Integer idLotacao) {
        return repository.buscaDiretorLotacao(idLotacao).map(ServidorJpaMapper::toDomain);
    }

    @Override
    public Servidor salva(Servidor servidor) {
        var servidorJpa = ServidorJpaMapper.toEntity(servidor);
        return ServidorJpaMapper.toDomain(repository.save(servidorJpa));
    }

    @Override
    public Optional<Servidor> buscaPorId(Integer id) {
        return repository.findById(id).map(ServidorJpaMapper::toDomain);
    }
}