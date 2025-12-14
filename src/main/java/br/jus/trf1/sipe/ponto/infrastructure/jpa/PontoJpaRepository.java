package br.jus.trf1.sipe.ponto.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PontoJpaRepository extends JpaRepository<PontoJpa, PontoJpaId> {

    /**
     * Busca pontoJpas por matrícula e intervalo de datas.
     *
     * @param matricula Matrícula (sigla seção subseção + codigo funcionário no SARH) do servidor com o prefixo.
     * @param inicio    Data inicial do intervalo.
     * @param fim       Data final do intervalo.
     * @return Lista de pontoJpas encontrados.
     */
    @Query("SELECT p FROM PontoJpa p WHERE LOWER(p.id.usuario.matricula) = LOWER(:matricula) AND p.id.dia BETWEEN :inicio AND :fim")
    List<PontoJpa> buscaPontosPorPeriodo(@Param("matricula") String matricula,
                                         @Param("inicio") LocalDate inicio,
                                         @Param("fim") LocalDate fim
    );

    /**
     * Busca um pontoJpa por matrícula e data.
     *
     * @param matricula Matrícula do pontoJpa.
     * @param dia       Data do pontoJpa.
     * @return Ponto encontrado, se existir.
     */
    @Query("SELECT p FROM PontoJpa p WHERE p.id.usuario.matricula = :matricula AND p.id.dia = :dia")
    Optional<PontoJpa> buscaPonto(
            @Param("matricula") String matricula,
            @Param("dia") LocalDate dia
    );

    @Query("""
            SELECT
            CASE WHEN (COUNT(pa) > 0) THEN TRUE ELSE FALSE END
            FROM
                 PontoJpa p
                 JOIN p.pedidos pa
             WHERE
                 p.id.usuario.matricula = :matricula
                 AND p.id.dia BETWEEN :dataInicio AND :dataFim
                 AND pa.dataAprovacao IS NULL
                 AND pa.alteracaoRegistros IS NOT EMPTY
            """)
    boolean existePontosComAlteracaoRegistroPendentePorData(@Param("matricula")String matricula,
                                                            @Param("dataInicio") LocalDate dataInicio,
                                                            @Param("dataFim") LocalDate dataFim);
}
