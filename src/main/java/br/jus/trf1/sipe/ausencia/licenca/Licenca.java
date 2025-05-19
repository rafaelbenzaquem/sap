package br.jus.trf1.sipe.ausencia.licenca;

import br.jus.trf1.sipe.ausencia.Ausencia;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "licencas_servidores", schema = "sispontodb")
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
