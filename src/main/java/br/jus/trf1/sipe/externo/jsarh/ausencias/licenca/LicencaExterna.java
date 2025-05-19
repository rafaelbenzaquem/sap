package br.jus.trf1.sipe.externo.jsarh.ausencias.licenca;

import br.jus.trf1.sipe.externo.jsarh.ausencias.AusenciaExterna;
import br.jus.trf1.sipe.ausencia.Ausencia;
import br.jus.trf1.sipe.ausencia.licenca.Licenca;
import br.jus.trf1.sipe.usuario.Usuario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class LicencaExterna extends AusenciaExterna {

    private String sei;

    public LicencaExterna(String id, LocalDate inicio, LocalDate fim, String descricao, String sei) {
        super(id, inicio, fim, "Licença: " + descricao);
        this.sei = sei;
    }

    @Override
    public Ausencia toModel(Usuario usuario) {
        return new Licenca(super.toModel(usuario), this.sei);
    }

}
