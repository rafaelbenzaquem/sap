package br.jus.trf1.sipe.servidor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface ServidorRepository extends JpaRepository<Servidor, Integer> {


    Optional<Servidor> findByMatricula(String matricula);



    @Query(value ="""
            SELECT s FROM Servidor s
            WHERE s.lotacao.id = :idLotacao
            """,
            countQuery = """
                    SELECT COUNT(s) FROM Servidor s
                                WHERE s.lotacao.id = :idLotacao
                    """)
    Page<Servidor> buscarPorLotacao(@Param("idLotacao") Integer idLotacao, Pageable pageable);


    @Query(value = """
        SELECT s FROM Servidor s
        WHERE s.lotacao.id IN :idsLotacoes
        """,
            countQuery = """
        SELECT COUNT(s) FROM Servidor s
        WHERE s.lotacao.id IN :idsLotacoes
        """)
    Page<Servidor> buscarPorLotacoes(@Param("idsLotacoes") Set<Integer> idsLotacoes, Pageable pageable);

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
    Page<Servidor> findAllByNomeOrCrachaOrMatricula(@Param("nome") String nome,
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
            AND s.lotacao.id = :idLotacao
            """,
            countQuery = """
                    SELECT COUNT(s) FROM Servidor s
                    WHERE (
                            LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
                            OR s.cracha=  :cracha
                            OR s.matricula = :matricula
                          )
                    AND (s.lotacao.id = :idLotacao)
                    """)
    Page<Servidor> findAllByNomeOrCrachaOrMatriculaAndIdLotacao(@Param("nome") String nome,
                                                                @Param("cracha") Integer cracha,
                                                                @Param("matricula") String matricula,
                                                                @Param("idLotacao") Integer idLotacao,
                                                                Pageable pageable);

}
