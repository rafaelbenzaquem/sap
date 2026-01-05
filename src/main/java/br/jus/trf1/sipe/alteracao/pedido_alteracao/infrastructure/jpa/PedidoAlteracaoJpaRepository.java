package br.jus.trf1.sipe.alteracao.pedido_alteracao.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

public interface PedidoAlteracaoJpaRepository extends JpaRepository<PedidoAlteracaoJpa, Long> {

    @Query(value = """
               select p from PedidoAlteracaoJpa p where p.ponto.id.usuario.matricula =LOWER(:matricula)
               and p.ponto.id.dia =:dia
            """)
    Optional<PedidoAlteracaoJpa> buscaPorPonto(@Param("matricula") String matricula, @Param("dia") LocalDate dia);


    @Query(value = """
               select p from PedidoAlteracaoJpa p where p.ponto.id.usuario.matricula =LOWER(:matricula)
               and p.ponto.id.dia =:dia and p.dataAprovacao IS NULL
            """)
    Optional<PedidoAlteracaoJpa> buscaPorPontoEmAprovacao(@Param("matricula") String matricula, @Param("dia") LocalDate dia);


    @Query("""
            SELECT pa FROM PedidoAlteracaoJpa pa WHERE pa.id =:id
            """)
    Optional<PedidoAlteracaoJpa> buscaPorId(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("""
              DELETE FROM PedidoAlteracaoJpa pa WHERE pa.id=:id
            """)
    void apagarPedidoAlteracaoPorId(@Param("id") Long id);
}
