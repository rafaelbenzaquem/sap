package br.jus.trf1.sipe.externo.jsarh.ausencias;

import br.jus.trf1.sipe.ausencia.Ausencia;
import br.jus.trf1.sipe.usuario.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class AusenciaExterna {
    private String id;
    private LocalDate inicio;
    private LocalDate fim;
    private String descricao;


    public Ausencia toModel(Usuario usuario) {
        return new Ausencia(this.id, this.inicio, this.fim, this.descricao, usuario);
    }

}
