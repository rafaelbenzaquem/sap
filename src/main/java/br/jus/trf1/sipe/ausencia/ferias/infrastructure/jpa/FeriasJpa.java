package br.jus.trf1.sipe.ausencia.ferias.infrastructure.jpa;

import br.jus.trf1.sipe.ausencia.ausencia.infrastructure.jpa.AusenciaJpa;
import br.jus.trf1.sipe.ausencia.ferias.domain.model.Ocorrencia;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "ferias_servidores", schema = "sispontodb")
public class FeriasJpa extends AusenciaJpa {

    private Ocorrencia flag;
    private Integer diasGozados;
    private LocalDateTime dataSuspensao;

    public FeriasJpa(AusenciaJpa ausencia, Ocorrencia flag, Integer diasGozados, LocalDateTime dataSuspensao) {
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
