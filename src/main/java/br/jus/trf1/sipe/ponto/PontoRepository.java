package br.jus.trf1.sipe.ponto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PontoRepository extends JpaRepository<Ponto, PontoId> {

    /**
     * Busca pontos por matrícula e intervalo de datas.
     *
     * @param matricula Matrícula (sigla seção subseção + codigo funcionário no SARH) do servidor com o prefixo.
     * @param inicio    Data inicial do intervalo.
     * @param fim       Data final do intervalo.
     * @return Lista de pontos encontrados.
     */
    @Query("SELECT p FROM Ponto p WHERE LOWER(p.id.matricula) = LOWER(:matricula) AND p.id.dia BETWEEN :inicio AND :fim")
    List<Ponto> buscaPontosPorPeriodo(@Param("matricula") String matricula,
                                      @Param("inicio") LocalDate inicio,
                                      @Param("fim") LocalDate fim
    );

    /**
     * Busca um ponto por matrícula e data.
     *
     * @param matricula Matrícula do ponto.
     * @param dia       Data do ponto.
     * @return Ponto encontrado, se existir.
     */
    @Query("SELECT p FROM Ponto p WHERE p.id.matricula = :matricula AND p.id.dia = :dia")
    Optional<Ponto> buscaPonto(
            @Param("matricula") String matricula,
            @Param("dia") LocalDate dia
    );
}
