package br.jus.trf1.sipe.folha;

import br.jus.trf1.sipe.servidor.Servidor;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

/**
 * Representa o fechamento mensal de folha de ponto de um servidor,
 * armazenando saldo de horas a compensar ou pagar e prazos.
 */
@Entity
@Table(name = "fechamentos_folhas", schema = "sispontodb",
       uniqueConstraints = @UniqueConstraint(
           columnNames = {"id_servidor", "mes", "ano"}, name = "uk_fechamento_folha"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FechamentoFolha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servidor", nullable = false,
        foreignKey = @ForeignKey(name = "fk_servidor_fechamento"))
    private Servidor servidor;

    @Column(nullable = false)
    private Integer mes;

    @Column(nullable = false)
    private Integer ano;

    /** Total de minutos efetivamente trabalhados no mês. */
    @Column(name = "horas_executadas_minutos", nullable = false)
    private Long horasExecutadasMinutos;

    /** Total de minutos esperados (horaDiaria * dias uteis). */
    @Column(name = "horas_esperadas_minutos", nullable = false)
    private Long horasEsperadasMinutos;

    /** Saldo de minutos: executados - esperados (positivo=crédito, negativo=débito). */
    @Column(name = "saldo_minutos", nullable = false)
    private Long saldoMinutos;

    /** Timestamp do fechamento da folha. */
    @Column(name = "data_fechamento", nullable = false)
    private Timestamp dataFechamento;

    /** Prazo para compensação ou pagamento: fechamento + 3 meses. */
    @Column(name = "prazo_compensacao", nullable = false)
    private Timestamp prazoCompensacao;
}