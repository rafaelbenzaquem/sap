package br.jus.trf1.sipe.ausencia.ferias.domain.model;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Ferias extends Ausencia {

    private Ocorrencia flag;
    private Integer diasGozados;
    private LocalDateTime dataSuspensao;

    public Ferias(Ausencia ausencia, Ocorrencia flag, Integer diasGozados, LocalDateTime dataSuspensao) {
        super(ausencia);
        this.flag = flag;
        this.diasGozados = diasGozados;
        this.dataSuspensao = dataSuspensao;
    }

    @Override
    public String toString() {
        return "Ferias{" +
                super.toString() + "\n" +
                "flag=" + flag + "\n" +
                ", diasGozados=" + diasGozados + "\n" +
                ", dataSuspensao=" + dataSuspensao + "\n" +
                '}';
    }
}
