package br.jus.trf1.sipe.fechamento.domain.model;

import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import lombok.*;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Representa o fechamento mensal de folha de ponto de um servidor,
 * armazenando saldo de horas a compensar ou pagar e prazos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fechamento {
    private Long id;

    private Servidor servidor;

    private Integer mes;

    private Integer ano;

    private Long horasExecutadasMinutos;

    private Long horasEsperadasMinutos;

    private Long saldoMinutos;

    private Timestamp dataFechamento;

    private Timestamp prazoCompensacao;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Fechamento that)) return false;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}