package br.jus.trf1.sipe.alteracao.pedido_alteracao;

import br.jus.trf1.sipe.alteracao.alteracao_registro.AlteracaoRegistro;
import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "pedidos_alteracoes", schema = "sispontodb")
public class PedidoAlteracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_solicitacao")
    @CreationTimestamp
    private Timestamp dataSolicitacao;

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    private String justificativa;

    @OneToMany(mappedBy = "peidoAlteracao", orphanRemoval = true)
    private List<AlteracaoRegistro> alteracaoRegistros;

    @ManyToOne
    @JoinColumn(name = "usuario_solicitante_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_pa_usuario_solicitante_id"))
    private Usuario usuarioSolicitante;

    @ManyToOne
    @JoinColumn(name = "usuario_aprovador_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_pa_usuario_aprovador_id"))
    private Usuario usuarioAprovador;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "ponto_matricula", referencedColumnName = "matricula", nullable = false),
            @JoinColumn(name = "ponto_dia", referencedColumnName = "dia", nullable = false)
    }, foreignKey = @ForeignKey(name = "fk_pa_ponto"))
    private Ponto ponto;

    public void setJustificativa(String justificativa) {
        this.justificativa = this.justificativa ==null? justificativa: this.justificativa +" - "+justificativa;
    }
}