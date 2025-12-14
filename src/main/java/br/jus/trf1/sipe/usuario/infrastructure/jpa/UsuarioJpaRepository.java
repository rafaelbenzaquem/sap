package br.jus.trf1.sipe.usuario.infrastructure.jpa;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioJpaRepository extends JpaRepository<UsuarioJpa, Integer> {

    @Query(value = """
            SELECT u FROM UsuarioJpa u
            WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
            OR u.cracha=  :cracha
            OR u.matricula = :matricula
            """,
            countQuery = """
                    SELECT COUNT(u) FROM UsuarioJpa u
                    WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
                    OR u.cracha=  :cracha
                    OR u.matricula = :matricula
                    """)
    Page<UsuarioJpa> paginaPorNomeOuCrachaOuMatricula(@Param("nome") String nome,
                                                      @Param("cracha") Integer cracha,
                                                      @Param("matricula") String matricula,
                                                      Pageable pageable);


    @Query(value = """
            SELECT u FROM UsuarioJpa u
            WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
            OR u.cracha=  :cracha
            OR u.matricula = :matricula
            """,
            countQuery = """
                    SELECT COUNT(u) FROM UsuarioJpa u
                    WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
                    OR u.cracha=  :cracha
                    OR u.matricula = :matricula
                    """)
    List<UsuarioJpa> listaPorNomeOuCrachaOuMatricula(@Param("nome") String nome,
                                                      @Param("cracha") Integer cracha,
                                                      @Param("matricula") String matricula);


    Optional<UsuarioJpa> findUsuarioByMatricula(String matricula);




    @Query(value = """
                    SELECT COUNT(u) FROM UsuarioJpa u
                    WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
                    OR u.cracha=  :cracha
                    OR u.matricula = :matricula
                    """)
    long countAllByNomeOrCrachaOrMatricula(@Param("nome") String nome,
                                                      @Param("cracha") Integer cracha,
                                                      @Param("matricula") String matricula);

    /**
     * Verifica a existência usando uma consulta JPQL explícita.
     * Retorna diretamente um boolean fazendo a comparação > 0 no JPQL.
     * Útil para consultas mais complexas ou quando a convenção de nome não se aplica bem.
     *
     * @param matricula O matricula a ser verificado.
     * @param id Identificador o UsuarioJpa que faz a requisição
     * @return true se um usuário com o matricula existir, false caso contrário.
     */
    @Query("SELECT COUNT(u.id) > 0 FROM UsuarioJpa u WHERE u.matricula = :matriculaParam AND u.id != :idParam")
    boolean checaSeExisteUsuarioComMatricula(@Param("matriculaParam") String matricula, @Param("idParam") Integer id);


    /**
     * Verifica a existência usando uma consulta JPQL explícita.
     * Retorna diretamente um boolean fazendo a comparação > 0 no JPQL.
     * Útil para consultas mais complexas ou quando a convenção de nome não se aplica bem.
     *
     * @param cracha O cracha a ser verificado.
     * @param id Identificador o UsuarioJpa que faz a requisição
     * @return true se um usuário com o cracha existir, false caso contrário.
     */
    @Query("SELECT COUNT(u.id) > 0 FROM UsuarioJpa u WHERE u.cracha = :cracha AND u.id != :idParam")
    boolean checaSeExisteUsuarioComCracha(@Param("cracha") Integer cracha, @Param("idParam") Integer id);
}
