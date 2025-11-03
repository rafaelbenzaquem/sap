package br.jus.trf1.sipe.servidor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ServidorRepository extends JpaRepository<Servidor, Integer> {


    Optional<Servidor> findByMatricula(String matricula);


    @Query(value = """
            SELECT s FROM Servidor s ORDER BY s.nome ASC
            """,
            countQuery = """
                    SELECT COUNT(s) FROM Servidor s
                    WHERE s.lotacao.id IN :idsLotacoes ORDER BY s.nome ASC
                    """)
    List<Servidor> listarTodos();

    @Query(value = """
            SELECT s FROM Servidor s
            WHERE s.lotacao.id IN :idsLotacoes ORDER BY s.nome ASC
            """,
            countQuery = """
                    SELECT COUNT(s) FROM Servidor s
                    WHERE s.lotacao.id IN :idsLotacoes ORDER BY s.nome ASC
                    """)
    List<Servidor> listarPorLotacoes(@Param("idsLotacoes") Set<Integer> idsLotacoes);

    @Query(value = """
             SELECT s FROM Servidor s
             WHERE LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
             OR s.cracha=  :cracha
             OR s.matricula = :matricula
             """,
            countQuery = """
                    SELECT COUNT(s) FROM Servidor s
                    WHERE LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
                    OR s.cracha=  :cracha
                    OR s.matricula = :matricula
                    """)
    List<Servidor> listarPorNomeOuCrachaOuMatricula(@Param("nome") String nome,
                                                              @Param("cracha") Integer cracha,
                                                              @Param("matricula") String matricula);


    @Query(value = """
            SELECT s FROM Servidor s
                        WHERE (
                                 LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
                                 OR s.cracha=  :cracha
                                 OR s.matricula = :matricula
                        )
                        AND s.lotacao.id IN :idsLotacoes ORDER BY s.nome ASC
            """,
            countQuery = """
                    SELECT COUNT(s) FROM Servidor s
                    WHERE (
                             LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
                             OR s.cracha=  :cracha
                             OR s.matricula = :matricula
                    )
                    AND s.lotacao.id IN :idsLotacoes ORDER BY s.nome ASC
                    """)
    List<Servidor> listarPorNomeOuCrachaOuMatriculaEeLotacoes(@Param("nome") String nome,
                                                              @Param("cracha") Integer cracha,
                                                              @Param("matricula") String matricula,
                                                              @Param("idsLotacoes") Set<Integer> idsLotacoes);


    @Query(value = """
            SELECT s FROM Servidor s
            WHERE s.lotacao.id IN :idsLotacoes ORDER BY s.nome ASC
            """,
            countQuery = """
                    SELECT COUNT(s) FROM Servidor s
                    WHERE s.lotacao.id IN :idsLotacoes ORDER BY s.nome ASC
                    """)
    Page<Servidor> paginarPorLotacoes(@Param("idsLotacoes") Set<Integer> idsLotacoes, Pageable pageable);


    @Query(value = """
            SELECT s FROM Servidor s
            WHERE LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
            OR s.cracha=  :cracha
            OR s.matricula = :matricula ORDER BY s.nome ASC
            """,
            countQuery = """
                    SELECT COUNT(s) FROM Servidor s
                    WHERE LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
                    OR s.cracha=  :cracha
                    OR s.matricula = :matricula order by s.nome asc
                    """)
    Page<Servidor> paginarPorNomeOuCrachaOuMatricula(@Param("nome") String nome,
                                                     @Param("cracha") Integer cracha,
                                                     @Param("matricula") String matricula,
                                                     Pageable pageable);

    @Query(value = """
            SELECT s FROM Servidor s
            WHERE (
                    LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
                    OR s.cracha=  :cracha
                    OR s.matricula = :matricula
                  )
            AND s.lotacao.id = :idLotacao ORDER BY s.nome ASC
            """,
            countQuery = """
                    SELECT COUNT(s) FROM Servidor s
                    WHERE (
                            LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
                            OR s.cracha=  :cracha
                            OR s.matricula = :matricula
                          )
                    AND (s.lotacao.id = :idLotacao) ORDER BY s.nome ASC
                    """)
    Page<Servidor> paginarPorNomeOuCrachaOuMatriculaEeIdLotacao(@Param("nome") String nome,
                                                                @Param("cracha") Integer cracha,
                                                                @Param("matricula") String matricula,
                                                                @Param("idLotacao") Integer idLotacao,
                                                                Pageable pageable);

}
