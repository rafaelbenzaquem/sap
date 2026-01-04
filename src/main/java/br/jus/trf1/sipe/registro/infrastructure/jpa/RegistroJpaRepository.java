package br.jus.trf1.sipe.registro.infrastructure.jpa;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RegistroJpaRepository extends JpaRepository<RegistroJpa, Long> {



    @Query("""
            SELECT r FROM RegistroJpa r WHERE r.id =:id
            """)
    Optional<RegistroJpa> buscaPorId(@Param("id") Long id);

    @Query("""
            SELECT r FROM RegistroJpa r WHERE r.ponto.id.usuario.matricula =:matricula AND r.ponto.id.dia =:dia
            AND r.registroNovo IS NULL ORDER BY r.hora ASC
            """)
    List<RegistroJpa> listaAtuaisDoPonto(@Param("matricula") String matricula,
                                         @Param("dia") LocalDate dia);

    @Query("""
            SELECT r FROM RegistroJpa r WHERE r.ponto.id.usuario.matricula =:matricula AND r.ponto.id.dia =:dia
            AND r.registroNovo IS NULL AND r.ativo IS TRUE ORDER BY r.hora ASC
            """)
    List<RegistroJpa> listaRegistrosAtuaisAtivosDoPonto(@Param("matricula") String matricula,
                                                        @Param("dia") LocalDate dia);
    @Query("""
            SELECT r FROM RegistroJpa r WHERE r.ponto.id.usuario.matricula =:matricula AND r.ponto.id.dia =:dia
            AND r.codigoAcesso IS NOT NULL ORDER BY r.hora ASC
            """)
    List<RegistroJpa> listaProvenientesDoSistemaExterno(@Param("matricula") String matricula,
                                                        @Param("dia") LocalDate dia);


    Optional<RegistroJpa> findByCodigoAcesso(Integer codigoAcesso);


    @Transactional
    @Modifying
    @Query("""
              DELETE FROM RegistroJpa r WHERE r.id=:id
            """)
    void apagarRegistroPorId(@Param("id") Long id);

}
