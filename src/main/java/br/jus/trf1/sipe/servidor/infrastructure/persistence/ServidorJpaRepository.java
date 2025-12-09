package br.jus.trf1.sipe.servidor.infrastructure.persistence;

import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ServidorJpaRepository extends JpaRepository<Servidor, Integer> {

    Optional<Servidor> findByMatricula(String matricula);

    @Query(value = "SELECT s FROM ServidorJpa s ORDER BY s.nome ASC", countQuery = "SELECT COUNT(s) FROM ServidorJpa s")
    List<Servidor> listarTodos();

    @Query(value = "SELECT s FROM ServidorJpa s WHERE s.lotacao.id IN :idsLotacoes ORDER BY s.nome ASC",
           countQuery = "SELECT COUNT(s) FROM ServidorJpa s WHERE s.lotacao.id IN :idsLotacoes")
    List<Servidor> listarPorLotacoes(@Param("idsLotacoes") Set<Integer> idsLotacoes);

    @Query(value = "SELECT s FROM ServidorJpa s WHERE LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%')) OR s.cracha = :cracha OR s.matricula = :matricula",
           countQuery = "SELECT COUNT(s) FROM ServidorJpa s WHERE LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%')) OR s.cracha = :cracha OR s.matricula = :matricula")
    List<Servidor> listarPorNomeOuCrachaOuMatricula(@Param("nome") String nome, @Param("cracha") Integer cracha, @Param("matricula") String matricula);

    @Query(value = "SELECT s FROM ServidorJpa s WHERE (LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%')) OR s.cracha = :cracha OR s.matricula = :matricula) AND s.lotacao.id IN :idsLotacoes ORDER BY s.nome ASC",
           countQuery = "SELECT COUNT(s) FROM ServidorJpa s WHERE (LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%')) OR s.cracha = :cracha OR s.matricula = :matricula) AND s.lotacao.id IN :idsLotacoes")
    List<Servidor> listarPorNomeOuCrachaOuMatriculaEeLotacoes(@Param("nome") String nome, @Param("cracha") Integer cracha, @Param("matricula") String matricula, @Param("idsLotacoes") Set<Integer> idsLotacoes);

    @Query(value = "SELECT s FROM ServidorJpa s WHERE s.lotacao.id IN :idsLotacoes ORDER BY s.nome ASC",
           countQuery = "SELECT COUNT(s) FROM ServidorJpa s WHERE s.lotacao.id IN :idsLotacoes")
    Page<Servidor> paginarPorLotacoes(@Param("idsLotacoes") Set<Integer> idsLotacoes, Pageable pageable);

    @Query(value = "SELECT s FROM ServidorJpa s WHERE LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%')) OR s.cracha = :cracha OR s.matricula = :matricula ORDER BY s.nome ASC",
           countQuery = "SELECT COUNT(s) FROM ServidorJpa s WHERE LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%')) OR s.cracha = :cracha OR s.matricula = :matricula")
    Page<Servidor> paginarPorNomeOuCrachaOuMatricula(@Param("nome") String nome, @Param("cracha") Integer cracha, @Param("matricula") String matricula, Pageable pageable);

    @Query(value = "SELECT s FROM ServidorJpa s WHERE (LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%')) OR s.cracha = :cracha OR s.matricula = :matricula) AND s.lotacao.id = :idLotacao ORDER BY s.nome ASC",
           countQuery = "SELECT COUNT(s) FROM ServidorJpa s WHERE (LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%')) OR s.cracha = :cracha OR s.matricula = :matricula) AND (s.lotacao.id = :idLotacao)")
    Page<Servidor> paginarPorNomeOuCrachaOuMatriculaEeIdLotacao(@Param("nome") String nome, @Param("cracha") Integer cracha, @Param("matricula") String matricula, @Param("idLotacao") Integer idLotacao, Pageable pageable);

    @Query("SELECT s FROM ServidorJpa s WHERE LOWER(s.funcao) LIKE LOWER(CONCAT('%', 'diretor', '%')) AND s.lotacao.id = :idLotacao ORDER BY s.nome ASC")
    Optional<Servidor> buscaDiretorLotacao(@Param("idLotacao") Integer idLotacao);
}