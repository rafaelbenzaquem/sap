package br.jus.trf1.sipe.servidor.domain.port.out;

import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ServidorRepositoryPort {

    Optional<Servidor> findByMatricula(String matricula);

    List<Servidor> listarTodos();

    List<Servidor> listarPorLotacoes(Set<Integer> idsLotacoes);

    List<Servidor> listarPorNomeOuCrachaOuMatricula(String nome, Integer cracha, String matricula);

    List<Servidor> listarPorNomeOuCrachaOuMatriculaEeLotacoes(String nome, Integer cracha, String matricula, Set<Integer> idsLotacoes);

    Page<Servidor> paginarPorLotacoes(Set<Integer> idsLotacoes, Pageable pageable);

    Page<Servidor> paginarPorNomeOuCrachaOuMatricula(String nome, Integer cracha, String matricula, Pageable pageable);

    Page<Servidor> paginarPorNomeOuCrachaOuMatriculaEeIdLotacao(String nome, Integer cracha, String matricula, Integer idLotacao, Pageable pageable);

    Optional<Servidor> buscaDiretorLotacao(Integer idLotacao);

    Servidor save(Servidor servidor);
    
    Optional<Servidor> findById(Integer id);
}