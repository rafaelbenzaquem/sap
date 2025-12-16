package br.jus.trf1.sipe.ausencia.especial.infrastructure.jpa;

import br.jus.trf1.sipe.ausencia.ausencia.infrastructure.jpa.AusenciaJpa;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "freq_especiais_servidores", schema = "sispontodb")
public class EspecialJpa extends AusenciaJpa {

    private String sei;

    public EspecialJpa(AusenciaJpa ausencia, String sei) {
        super(ausencia);
        this.sei = sei;
    }

    @Override
    public String toString() {
        return "EspecialJpa{" +
                super.toString() + '\'' +
                "sei='" + sei + '\'' +
                '}';
    }
}
