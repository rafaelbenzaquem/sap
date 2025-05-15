package br.jus.trf1.sipe.externo.jsarh.ausencias.especial;

import br.jus.trf1.sipe.externo.jsarh.ausencias.AusenciaExternal;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class FrequenciaEspecialExterno extends AusenciaExternal {

    private String sei;

    public FrequenciaEspecialExterno(LocalDate inicio, LocalDate fim, String descricao, String sei) {
        super(inicio, fim, "Frequência Especial: " + descricao);
        this.sei = sei;
    }

}
