package br.jus.trf1.sap.vinculo;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VinculoRepository extends JpaRepository<Vinculo, Integer> {

    @Query(value = """
            SELECT v FROM servidor_matricula_cracha v
            WHERE LOWER(v.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
            OR LOWER(v.cracha) LIKE LOWER(CONCAT('%', :cracha, '%'))
            OR v.matricula = :matricula""",
            countQuery = """
            SELECT COUNT(v) FROM servidor_matricula_cracha v
            WHERE LOWER(v.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
            OR LOWER(v.cracha) LIKE LOWER(CONCAT('%', :cracha, '%'))
            OR v.matricula = :matricula""")
    Page<Vinculo> findAllByNomeOrCrachaOrMatricula(@Param("nome") String nome,
                                                   @Param("cracha") String cracha,
                                                   @Param("matricula") String matricula,
                                                   Pageable pageable);

    Optional<Vinculo> findVinculoByMatricula(String matricula);
}
