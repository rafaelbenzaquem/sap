package br.jus.trf1.sipe.alteracao.pedido_alteracao;

import br.jus.trf1.sipe.registro.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

public interface PedidoAlteracaoRepository extends JpaRepository<PedidoAlteracao, Long> {

    @Query(value = """
               select p from PedidoAlteracao p where p.ponto.id.usuarioJPA.matricula =LOWER(:matricula) and p.ponto.id.dia =:dia
            """)
    Optional<PedidoAlteracao> buscaPorPonto(@Param("matricula") String matricula, @Param("dia") LocalDate dia);


    @Query(value = """
               select p from PedidoAlteracao p where p.ponto.id.usuarioJPA.matricula =LOWER(:matricula) and p.ponto.id.dia =:dia and p.dataAprovacao IS NULL
            """)
    Optional<PedidoAlteracao> buscaPorPontoEmAprovacao(@Param("matricula") String matricula, @Param("dia") LocalDate dia);


    @Query("""
            SELECT pa FROM PedidoAlteracao pa WHERE pa.id =:id
            """)
    Optional<PedidoAlteracao> buscaPedidoAlteracaoPorId(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("""
              DELETE FROM PedidoAlteracao pa WHERE pa.id=:id
            """)
    void apagarPedidoAlteracaoPorId(@Param("id") Long id);
}
