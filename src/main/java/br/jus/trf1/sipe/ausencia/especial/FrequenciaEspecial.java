package br.jus.trf1.sipe.ausencia.especial;

import br.jus.trf1.sipe.ausencia.Ausencia;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "freq_especiais_servidores", schema = "sispontodb")
public class FrequenciaEspecial extends Ausencia {

    private String sei;

    public FrequenciaEspecial(Ausencia ausencia, String sei) {
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
