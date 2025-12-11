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

    List<Servidor> paginar(int page, int size);

    List<Servidor> paginarPorLotacoes(Set<Integer> idsLotacoes,int page, int size);

    List<Servidor> paginarPorNomeOuCrachaOuMatricula(String nome, Integer cracha, String matricula, int page, int size);

    List<Servidor> paginarPorNomeOuCrachaOuMatriculaEeIdLotacao(String nome, Integer cracha, String matricula, Integer idLotacao, int page, int size);

    Optional<Servidor> buscaDiretorLotacao(Integer idLotacao);

    Servidor save(Servidor servidor);
    
    Optional<Servidor> findById(Integer id);
}