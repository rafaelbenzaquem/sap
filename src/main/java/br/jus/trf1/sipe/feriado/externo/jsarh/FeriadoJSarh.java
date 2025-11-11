package br.jus.trf1.sipe.feriado.externo.jsarh;

import br.jus.trf1.sipe.feriado.Feriado;

import java.time.LocalDate;

public class FeriadoJSarh extends Feriado {


    public FeriadoJSarh(LocalDate data, String descricao, Integer abrangencia, Integer tipo) {
        super(data, descricao, abrangencia, tipo);
    }
}
