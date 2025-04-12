package br.jus.trf1.sipe.externo.jsarh.ausencias.especial;

import br.jus.trf1.sipe.externo.jsarh.ausencias.Ausencia;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class Especial extends Ausencia {

    private String sei;

    public Especial(LocalDate inicio, LocalDate fim, String descricao, String sei) {
        super(inicio, fim, "Especial: " + descricao);
        this.sei = sei;
    }

}
