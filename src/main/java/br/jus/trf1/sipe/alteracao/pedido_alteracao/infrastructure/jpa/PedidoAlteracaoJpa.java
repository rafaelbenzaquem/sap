package br.jus.trf1.sipe.alteracao.pedido_alteracao.infrastructure.jpa;

import br.jus.trf1.sipe.alteracao.alteracao_registro.infrastructure.jpa.AlteracaoRegistroJpa;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.StatusPedido;
import br.jus.trf1.sipe.ponto.infrastructure.jpa.PontoJpa;
import br.jus.trf1.sipe.usuario.infrastructure.jpa.UsuarioJpa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "pedidos_alteracoes", schema = "sispontodb")
public class PedidoAlteracaoJpa {

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

    @Column(name = "justificativa_aprovador", nullable = true)
    private String justificativaAprovador;

    @OneToMany(mappedBy = "peidoAlteracao", orphanRemoval = true)
    private List<AlteracaoRegistroJpa> alteracaoRegistros;

    @ManyToOne
    @JoinColumn(name = "usuario_solicitante_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_pa_usuario_solicitante_id"))
    private UsuarioJpa usuarioSolicitante;

    @ManyToOne
    @JoinColumn(name = "usuario_aprovador_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_pa_usuario_aprovador_id"))
    private UsuarioJpa usuarioAprovador;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "ponto_matricula", referencedColumnName = "matricula", nullable = false),
            @JoinColumn(name = "ponto_dia", referencedColumnName = "dia", nullable = false)
    }, foreignKey = @ForeignKey(name = "fk_pa_ponto"))
    private PontoJpa ponto;

    public void setJustificativa(String justificativa) {
        this.justificativa = this.justificativa ==null? justificativa: this.justificativa +" - "+justificativa;
    }
}