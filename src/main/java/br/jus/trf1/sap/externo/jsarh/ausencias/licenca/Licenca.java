package br.jus.trf1.sap.externo.jsarh.ausencias.licenca;

import br.jus.trf1.sap.externo.jsarh.ausencias.Ausencia;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class Licenca extends Ausencia {

    private String sei;

    public Licenca(LocalDate inicio, LocalDate fim, String descricao, String sei) {
        super(inicio, fim,descricao);
        this.sei = sei;
    }

}
