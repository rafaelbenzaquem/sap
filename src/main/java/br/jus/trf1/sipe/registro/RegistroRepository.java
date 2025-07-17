package br.jus.trf1.sipe.registro;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RegistroRepository extends JpaRepository<Registro, Long> {

    @Query("""
            SELECT r FROM Registro r WHERE r.ponto.id.matricula =:matricula AND r.ponto.id.dia =:dia
            AND r.registroNovo IS NULL AND r.ativo IS TRUE ORDER BY r.hora ASC
            """)
    List<Registro> listarRegistrosAtuaisAtivosDoPonto(@Param("matricula") String matricula,
                                                      @Param("dia") LocalDate dia);

    @Query("""
            SELECT r FROM Registro r WHERE r.ponto.id.matricula =:matricula AND r.ponto.id.dia =:dia
            AND r.registroNovo IS NULL ORDER BY r.hora ASC
            """)
    List<Registro> listarRegistrosAtuaisDoPonto(@Param("matricula") String matricula,
                                                @Param("dia") LocalDate dia);


    @Query("""
            SELECT r FROM Registro r WHERE r.ponto.id.matricula =:matricula AND r.ponto.id.dia =:dia
            AND r.codigoAcesso IS NOT NULL ORDER BY r.hora ASC
            """)
    List<Registro> listarRegistrosHistoricosDoPonto(@Param("matricula") String matricula,
                                                    @Param("dia") LocalDate dia);


    Optional<Registro> findByCodigoAcesso(Integer codigoAcesso);


    @Transactional
    @Modifying
    @Query("""
              DELETE FROM Registro r WHERE r.id=:id
            """)
    void apagarRegistroPorId(@Param("id") Long id);

}
