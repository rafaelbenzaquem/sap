package br.jus.trf1.sipe.ausencia.ausencia.infrastructure.jsarh;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class AusenciaJSarh {
    private String id;
    private LocalDate inicio;
    private LocalDate fim;
    private String descricao;


    public Ausencia toModel(Usuario usuario) {
        return new Ausencia(this.id, this.inicio, this.fim, this.descricao, usuario);
    }

}
