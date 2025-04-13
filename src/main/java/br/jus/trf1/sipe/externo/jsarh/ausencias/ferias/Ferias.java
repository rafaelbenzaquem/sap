package br.jus.trf1.sipe.externo.jsarh.ausencias.ferias;

import br.jus.trf1.sipe.externo.jsarh.ausencias.Ausencia;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class Ferias extends Ausencia {

    private Ocorrencias flag;
    private Integer diasGozados;
    private LocalDateTime dataSuspensao;

    public Ferias(LocalDate inicio, LocalDate fim, Ocorrencias flag,
                  Integer diasGozados, LocalDateTime dataSuspensao) {
        super(inicio, fim,"Férias.");
        this.flag = flag;
        this.diasGozados = diasGozados;
        this.dataSuspensao = dataSuspensao;
    }

}
