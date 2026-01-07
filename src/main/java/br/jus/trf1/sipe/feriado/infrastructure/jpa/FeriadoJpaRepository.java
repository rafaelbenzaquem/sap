package br.jus.trf1.sipe.feriado.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface FeriadoJpaRepository extends JpaRepository<FeriadoJpa, LocalDate> {

    @Query("""
                SELECT f FROM FeriadoJpa f
                WHERE f.data BETWEEN :inicio AND :fim
            """)
    List<FeriadoJpa> lista(@Param("inicio") LocalDate inicio,@Param("fim") LocalDate fim);
}
