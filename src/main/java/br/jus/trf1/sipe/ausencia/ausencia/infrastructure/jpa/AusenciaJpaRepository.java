package br.jus.trf1.sipe.ausencia.ausencia.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AusenciaJpaRepository extends JpaRepository<AusenciaJpa, Long> {


    @Query("""
            SELECT a FROM AusenciaJpa a WHERE a.usuario.matricula = :matricula
            AND (a.inicio BETWEEN :inicio AND :fim
            OR a.fim BETWEEN :inicio AND :fim)
            """)
    List<AusenciaJpa> listaAusenciasDoServidorNoPeriodo(@Param("matricula") String matricula,
                                                        @Param("inicio") LocalDate inicio,
                                                        @Param("fim") LocalDate fim);

    @Query("""
            SELECT a FROM AusenciaJpa a WHERE a.usuario.matricula = :matricula
            AND (a.inicio BETWEEN :dia AND :dia
            OR a.fim BETWEEN :dia AND :dia)
            """)
    Optional<AusenciaJpa> buscaAusenciaDoServidorNoDia(@Param("matricula") String matricula,
                                                       @Param("dia") LocalDate dia);

}
