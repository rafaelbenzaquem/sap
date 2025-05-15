package br.jus.trf1.sipe.externo.jsarh.ausencias.licenca;

import br.jus.trf1.sipe.externo.jsarh.ausencias.AusenciaExternal;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class LicencaExternal extends AusenciaExternal {

    private String sei;

    public LicencaExternal(LocalDate inicio, LocalDate fim, String descricao, String sei) {
        super(inicio, fim,"Licença: "+descricao);
        this.sei = sei;
    }

}
