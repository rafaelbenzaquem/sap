package br.jus.trf1.sipe.alteracao.alteracao_registro.infrastructure.jpa;

import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.Acao;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.infrastructure.jpa.PedidoAlteracaoJpa;
import br.jus.trf1.sipe.registro.Registro;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alteracoes_registros", schema = "sispontodb")
public class AlteracaoRegistroJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Acao acao;


    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "pedido_alteracao_id", referencedColumnName = "id", nullable = false)
    }, foreignKey = @ForeignKey(name = "fk_ar_pedido_alteracao_id"))
    private PedidoAlteracaoJpa peidoAlteracao;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "registro_original_id", referencedColumnName = "id")
    }, foreignKey = @ForeignKey(name = "fk_ar_registro_original_id"))
    private Registro registroOriginal;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "registro_novo_id", referencedColumnName = "id")
    }, foreignKey = @ForeignKey(name = "fk_ar_registro_novo_id"))
    private Registro registroNovo;

}