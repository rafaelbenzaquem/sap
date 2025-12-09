package br.jus.trf1.sipe.lotacao.infrastructure.persistence;

import br.jus.trf1.sipe.lotacao.domain.model.Lotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface LotacaoJpaRepository extends JpaRepository<Lotacao, Integer> {

    @Query(value = """
        WITH RECURSIVE lotacoes_rec AS (
            SELECT l.id, l.id_lotacao_pai, l.sigla, l.descricao
            FROM sisponto.lotacoes l
            WHERE l.id = :lotacaoId
            UNION ALL
            SELECT l2.id, l2.id_lotacao_pai, l2.sigla, l2.descricao
            FROM sisponto.lotacoes l2
            INNER JOIN lotacoes_rec lr ON lr.id = l2.id_lotacao_pai
        )
        SELECT * FROM lotacoes_rec
        """, nativeQuery = true)
    Set<Lotacao> findLotacoesRecursivas(@Param("lotacaoId") Integer lotacaoId);
}