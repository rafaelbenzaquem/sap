package br.jus.trf1.sipe.ausencia.especial.domain.model;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Especial extends Ausencia {

    private String sei;

    public Especial(Ausencia ausencia, String sei) {
        super(ausencia);
        this.sei = sei;
    }

    @Override
    public String toString() {
        return "FrequenciaEspecial{" +
                super.toString() + '\'' +
                "sei='" + sei + '\'' +
                '}';
    }
}
