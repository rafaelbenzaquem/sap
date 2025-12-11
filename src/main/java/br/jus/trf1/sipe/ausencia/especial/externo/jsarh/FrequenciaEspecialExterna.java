package br.jus.trf1.sipe.ausencia.especial.externo.jsarh;

import br.jus.trf1.sipe.ausencia.externo.jsrh.AusenciaExterna;
import br.jus.trf1.sipe.ausencia.Ausencia;
import br.jus.trf1.sipe.ausencia.especial.FrequenciaEspecial;
import br.jus.trf1.sipe.usuario.infrastructure.db.UsuarioJpa;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class FrequenciaEspecialExterna extends AusenciaExterna {

    private String sei;

    public FrequenciaEspecialExterna(String id, LocalDate inicio, LocalDate fim, String descricao, String sei) {
        super(id, inicio, fim, "Frequência Especial: " + descricao);
        this.sei = sei;
    }


    @Override
    public Ausencia toModel(UsuarioJpa usuarioJPA) {
        return new FrequenciaEspecial(super.toModel(usuarioJPA), this.sei);
    }
}
