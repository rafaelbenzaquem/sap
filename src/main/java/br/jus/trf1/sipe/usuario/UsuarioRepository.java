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
            OR LOWER(u.cracha) LIKE LOWER(CONCAT('%', :cracha, '%'))
            OR u.matricula = :matricula
            """,
            countQuery = """
                    SELECT COUNT(u) FROM Usuario u
                    WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
                    OR LOWER(u.cracha) LIKE LOWER(CONCAT('%', :cracha, '%'))
                    OR u.matricula = :matricula
                    """)
    Page<Usuario> findAllByNomeOrCrachaOrMatricula(@Param("nome") String nome,
                                                   @Param("cracha") String cracha,
                                                   @Param("matricula") String matricula,
                                                   Pageable pageable);

    Optional<Usuario> findUsuarioByMatricula(String matricula);
}
