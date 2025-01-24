package br.jus.trf1.sap.ponto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PontoRepository extends JpaRepository<Ponto, PontoId> {

    @Query(nativeQuery = true, value = """
            SELECT * FROM PONTO WHERE PONTO.MATRICULA = :matricula AND
            PONTO.DIA BETWEEN :inicio AND :fim                                            
            """)
    List<Ponto> buscarPontosPorMatriculaMaisRangeDeData(@Param("matricula") Integer matricula,
                                                        @Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}
