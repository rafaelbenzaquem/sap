package br.jus.trf1.sipe.ausencia.ferias.infrastructure.jsarh;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;
import br.jus.trf1.sipe.ausencia.ausencia.infrastructure.jsarh.AusenciaJSarh;
import br.jus.trf1.sipe.ausencia.ferias.domain.model.Ferias;
import br.jus.trf1.sipe.ausencia.ferias.domain.model.Ocorrencia;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class FeriasJSarh extends AusenciaJSarh {

    private OcorrenciaJSarh flag;
    private Integer diasGozados;
    private LocalDateTime dataSuspensao;

    public FeriasJSarh(String id, LocalDate inicio, LocalDate fim, OcorrenciaJSarh flag,
                       Integer diasGozados, LocalDateTime dataSuspensao) {
        super(id, inicio, fim, "Férias.");
        this.flag = flag;
        this.diasGozados = diasGozados;
        this.dataSuspensao = dataSuspensao;
    }


    @Override
    public Ausencia toModel(Usuario usuario) {
        return new Ferias(super.toModel(usuario), Ocorrencia.valueOf(this.flag.getValor()), this.diasGozados, this.dataSuspensao);
    }
}
