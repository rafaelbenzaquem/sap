package br.jus.trf1.sipe.alteracao.alteracao_registro.infrastructure.jpa;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.infrastructure.jpa.PedidoAlteracaoJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlteracaoRegistroJpaRepository extends JpaRepository<AlteracaoRegistroJpa, Long> {

    @Query("""
    SELECT ar FROM AlteracaoRegistroJpa ar
    WHERE ar.pedidoAlteracao.id = :id
""")
    List<AlteracaoRegistroJpa> listaPorPedidoAlteracao(@Param("id") Long id);
}
