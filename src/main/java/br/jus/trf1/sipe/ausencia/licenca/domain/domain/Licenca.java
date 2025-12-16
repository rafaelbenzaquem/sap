package br.jus.trf1.sipe.ausencia.licenca.domain.domain;

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
public class Licenca extends Ausencia {

    private String sei;

    public Licenca(Ausencia ausencia, String sei) {
        super(ausencia);
        this.sei = sei;
    }

    @Override
    public String
    toString() {
        return "Licenca{" +
                super.toString() + '\'' +
                "sei='" + sei + '\'' +
                '}';
    }
}
