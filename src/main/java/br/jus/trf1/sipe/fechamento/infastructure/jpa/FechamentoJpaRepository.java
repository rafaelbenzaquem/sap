package br.jus.trf1.sipe.fechamento.infastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositório para o histórico de fechamentos de folha.
 */
public interface FechamentoJpaRepository extends JpaRepository<FechamentoJpa, Long> {
    /**
     * Localiza fechamento por matrícula, mês e ano.
     */
    Optional<FechamentoJpa> findByServidorMatriculaAndMesAndAno(
            String matricula, Integer mes, Integer ano);
}