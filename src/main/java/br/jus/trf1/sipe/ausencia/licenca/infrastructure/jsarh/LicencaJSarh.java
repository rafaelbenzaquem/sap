package br.jus.trf1.sipe.ausencia.licenca.infrastructure.jsarh;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;
import br.jus.trf1.sipe.ausencia.ausencia.infrastructure.jsarh.AusenciaJSarh;
import br.jus.trf1.sipe.ausencia.licenca.domain.domain.Licenca;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class LicencaJSarh extends AusenciaJSarh {

    private String sei;

    public LicencaJSarh(String id, LocalDate inicio, LocalDate fim, String descricao, String sei) {
        super(id, inicio, fim, "Licença: " + descricao);
        this.sei = sei;
    }

    @Override
    public Ausencia toModel(Usuario usuario) {
        return new Licenca(super.toModel(usuario), this.sei);
    }

}
