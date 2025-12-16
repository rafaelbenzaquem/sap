package br.jus.trf1.sipe.ausencia.ausencia.infrastructure.jpa;

import br.jus.trf1.sipe.usuario.infrastructure.jpa.UsuarioJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AusenciaJpaRepository extends JpaRepository<AusenciaJpa, Long> {


    @Query("""
            SELECT a FROM AusenciaJpa a WHERE a.usuario = :usuario
            AND (a.inicio BETWEEN :inicio AND :fim
            OR a.fim BETWEEN :inicio AND :fim)
            """)
    List<AusenciaJpa> listaAusenciasPorServidorMaisPeriodo(@Param("usuario") UsuarioJpa usuario,
                                  @Param("inicio") LocalDate inicio,
                                  @Param("fim") LocalDate fim);

}
