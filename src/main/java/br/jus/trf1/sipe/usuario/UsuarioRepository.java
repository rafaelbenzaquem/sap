package br.jus.trf1.sipe.usuario;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Query(value = """
            SELECT u FROM Usuario u
            WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
            OR u.cracha=  :cracha
            OR u.matricula = :matricula ORDER BY u.nome ASC
            """,
            countQuery = """
                    SELECT COUNT(u) FROM Usuario u
                    WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
                    OR u.cracha=  :cracha
                    OR u.matricula = :matricula order by u.nome ASC
                    """)
    Page<Usuario> findAllByNomeOrCrachaOrMatricula(@Param("nome") String nome,
                                                   @Param("cracha") Integer cracha,
                                                   @Param("matricula") String matricula,
                                                   Pageable pageable);

    Optional<Usuario> findUsuarioByMatricula(String matricula);

    /**
     * Verifica a existência usando uma consulta JPQL explícita.
     * Retorna diretamente um boolean fazendo a comparação > 0 no JPQL.
     * Útil para consultas mais complexas ou quando a convenção de nome não se aplica bem.
     *
     * @param matricula O matricula a ser verificado.
     * @param id Identificador o Usuario que faz a requisição
     * @return true se um usuário com o matricula existir, false caso contrário.
     */
    @Query("SELECT COUNT(u.id) > 0 FROM Usuario u WHERE u.matricula = :matriculaParam AND u.id != :idParam")
    boolean checaSeExisteUsuarioComMatricula(@Param("matriculaParam") String matricula, @Param("idParam") Integer id);


    /**
     * Verifica a existência usando uma consulta JPQL explícita.
     * Retorna diretamente um boolean fazendo a comparação > 0 no JPQL.
     * Útil para consultas mais complexas ou quando a convenção de nome não se aplica bem.
     *
     * @param cracha O cracha a ser verificado.
     * @param id Identificador o Usuario que faz a requisição
     * @return true se um usuário com o cracha existir, false caso contrário.
     */
    @Query("SELECT COUNT(u.id) > 0 FROM Usuario u WHERE u.cracha = :cracha AND u.id != :idParam")
    boolean checaSeExisteUsuarioComCracha(@Param("cracha") Integer cracha, @Param("idParam") Integer id);
}
