package br.jus.trf1.sipe.servidor.infrastructure.jpa;

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
public interface ServidorJpaRepository extends JpaRepository<ServidorJpa, Integer> {

    Optional<ServidorJpa> findByMatricula(String matricula);

    @Query(value = "SELECT s FROM ServidorJpa s ORDER BY s.nome ASC", countQuery = "SELECT COUNT(s) FROM ServidorJpa s")
    List<ServidorJpa> listarTodos();

    @Query(value = "SELECT s FROM ServidorJpa s WHERE s.lotacao.id IN :idsLotacoes ORDER BY s.nome ASC",
           countQuery = "SELECT COUNT(s) FROM ServidorJpa s WHERE s.lotacao.id IN :idsLotacoes")
    List<ServidorJpa> listarPorLotacoes(@Param("idsLotacoes") Set<Integer> idsLotacoes);

    @Query(value = "SELECT s FROM ServidorJpa s WHERE LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%')) OR s.cracha = :cracha OR s.matricula = :matricula",
           countQuery = "SELECT COUNT(s) FROM ServidorJpa s WHERE LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%')) OR s.cracha = :cracha OR s.matricula = :matricula")
    List<ServidorJpa> listarPorNomeOuCrachaOuMatricula(@Param("nome") String nome, @Param("cracha") Integer cracha, @Param("matricula") String matricula);

    @Query(value = "SELECT s FROM ServidorJpa s WHERE (LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%')) OR s.cracha = :cracha OR s.matricula = :matricula) AND s.lotacao.id IN :idsLotacoes ORDER BY s.nome ASC",
           countQuery = "SELECT COUNT(s) FROM ServidorJpa s WHERE (LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%')) OR s.cracha = :cracha OR s.matricula = :matricula) AND s.lotacao.id IN :idsLotacoes")
    List<ServidorJpa> listarPorNomeOuCrachaOuMatriculaEeLotacoes(@Param("nome") String nome, @Param("cracha") Integer cracha, @Param("matricula") String matricula, @Param("idsLotacoes") Set<Integer> idsLotacoes);

    @Query(value = "SELECT s FROM ServidorJpa s WHERE s.lotacao.id IN :idsLotacoes ORDER BY s.nome ASC",
           countQuery = "SELECT COUNT(s) FROM ServidorJpa s WHERE s.lotacao.id IN :idsLotacoes")
    Page<ServidorJpa> paginarPorLotacoes(@Param("idsLotacoes") Set<Integer> idsLotacoes, Pageable pageable);

    @Query(value = "SELECT s FROM ServidorJpa s WHERE LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%')) OR s.cracha = :cracha OR s.matricula = :matricula ORDER BY s.nome ASC",
           countQuery = "SELECT COUNT(s) FROM ServidorJpa s WHERE LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%')) OR s.cracha = :cracha OR s.matricula = :matricula")
    Page<ServidorJpa> paginarPorNomeOuCrachaOuMatricula(@Param("nome") String nome, @Param("cracha") Integer cracha, @Param("matricula") String matricula, Pageable pageable);

    @Query(value = "SELECT s FROM ServidorJpa s WHERE (LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%')) OR s.cracha = :cracha OR s.matricula = :matricula) AND s.lotacao.id = :idLotacao ORDER BY s.nome ASC",
           countQuery = "SELECT COUNT(s) FROM ServidorJpa s WHERE (LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%')) OR s.cracha = :cracha OR s.matricula = :matricula) AND (s.lotacao.id = :idLotacao)")
    Page<ServidorJpa> paginarPorNomeOuCrachaOuMatriculaEeIdLotacao(@Param("nome") String nome, @Param("cracha") Integer cracha, @Param("matricula") String matricula, @Param("idLotacao") Integer idLotacao, Pageable pageable);

    @Query("SELECT s FROM ServidorJpa s WHERE LOWER(s.funcao) LIKE LOWER(CONCAT('%', 'diretor', '%')) AND s.lotacao.id = :idLotacao ORDER BY s.nome ASC")
    Optional<ServidorJpa> buscaDiretorLotacao(@Param("idLotacao") Integer idLotacao);
}