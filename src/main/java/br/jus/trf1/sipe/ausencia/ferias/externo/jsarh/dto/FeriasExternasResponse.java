package br.jus.trf1.sipe.ausencia.ferias.externo.jsarh.dto;

import br.jus.trf1.sipe.ausencia.ferias.externo.jsarh.FeriasExternas;
import br.jus.trf1.sipe.ausencia.ferias.externo.jsarh.OcorrenciaExterna;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FeriasExternasResponse(Integer id,
                                     @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate inicio,
                                     @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate fim,
                                     @JsonProperty(value = "dias_gozados") Integer diasGozados,
                                     @JsonProperty(value = "data_suspensao")
                                     @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
                                     LocalDateTime dataSuspensao,
                                     String flag) {

    public FeriasExternas toModel() {
        return new FeriasExternas("ferias:"+id, inicio, fim, OcorrenciaExterna.parse(flag),
                diasGozados, dataSuspensao);
    }
}
