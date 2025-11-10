package br.jus.trf1.sipe.ausencia.ferias.externo.jsarh;

import br.jus.trf1.sipe.ausencia.externo.jsrh.AusenciaExterna;
import br.jus.trf1.sipe.ausencia.Ausencia;
import br.jus.trf1.sipe.ausencia.ferias.Ferias;
import br.jus.trf1.sipe.ausencia.ferias.Ocorrencia;
import br.jus.trf1.sipe.usuario.Usuario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class FeriasExternas extends AusenciaExterna {

    private OcorrenciaExterna flag;
    private Integer diasGozados;
    private LocalDateTime dataSuspensao;

    public FeriasExternas(String id, LocalDate inicio, LocalDate fim, OcorrenciaExterna flag,
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
