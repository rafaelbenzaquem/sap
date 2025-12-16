package br.jus.trf1.sipe.ausencia.licenca.infrastructure.jpa;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;
import br.jus.trf1.sipe.ausencia.ausencia.infrastructure.jpa.AusenciaJpa;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "licencas_servidores", schema = "sispontodb")
public class LicencaJpa extends AusenciaJpa {

    private String sei;

    public LicencaJpa(AusenciaJpa ausencia, String sei) {
        super(ausencia);
        this.sei = sei;
    }

    @Override
    public String
    toString() {
        return "LicencaJpa{" +
                super.toString() + '\'' +
                "sei='" + sei + '\'' +
                '}';
    }
}
