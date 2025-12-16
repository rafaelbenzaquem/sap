package br.jus.trf1.sipe.ausencia.especial.infrastructure.jsarh;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;
import br.jus.trf1.sipe.ausencia.ausencia.infrastructure.jsarh.AusenciaJSarh;
import br.jus.trf1.sipe.ausencia.especial.domain.model.Especial;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class EspecialJSarh extends AusenciaJSarh {

    private String sei;

    public EspecialJSarh(String id, LocalDate inicio, LocalDate fim, String descricao, String sei) {
        super(id, inicio, fim, "Frequência Especial: " + descricao);
        this.sei = sei;
    }

    @Override
    public Ausencia toModel(Usuario usuario) {
        return new Especial(super.toModel(usuario), this.sei);
    }
}
