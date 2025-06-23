package br.jus.trf1.sipe.folha;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositório para o histórico de fechamentos de folha.
 */
public interface FechamentoFolhaRepository extends JpaRepository<FechamentoFolha, Long> {
    /**
     * Localiza fechamento por matrícula, mês e ano.
     */
    Optional<FechamentoFolha> findByServidorMatriculaAndMesAndAno(
            String matricula, Integer mes, Integer ano);
}