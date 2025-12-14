package br.jus.trf1.sipe.folha.domain.model;

import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class FolhaId {

    private Servidor servidor;

    private Integer mes;

    private Integer ano;

    public Mes getMes() {
        return Mes.getMes(mes);
    }

    public void setMes(Mes mes) {
        this.mes = mes.getValor();
    }


    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof FolhaId folhaId)) return false;

        return Objects.equals(servidor, folhaId.servidor) && Objects.equals(mes, folhaId.mes) && Objects.equals(ano, folhaId.ano);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(servidor);
        result = 31 * result + Objects.hashCode(mes);
        result = 31 * result + Objects.hashCode(ano);
        return result;
    }
}
