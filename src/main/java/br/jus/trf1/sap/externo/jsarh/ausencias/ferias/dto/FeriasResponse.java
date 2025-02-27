package br.jus.trf1.sap.externo.jsarh.ausencias.ferias.dto;

import br.jus.trf1.sap.externo.jsarh.ausencias.ferias.Ferias;
import br.jus.trf1.sap.externo.jsarh.ausencias.ferias.Ocorrencias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FeriasResponse(@JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate inicio,
                             @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate fim,
                             @JsonProperty(value = "dias_gozados") Integer diasGozados,
                             @JsonProperty(value = "data_suspensao")
                             @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
                             LocalDateTime dataSuspensao,
                             String flag) {

    public Ferias toModel() {
        return new Ferias(inicio, fim, Ocorrencias.parse(flag),
                diasGozados, dataSuspensao);
    }
}
