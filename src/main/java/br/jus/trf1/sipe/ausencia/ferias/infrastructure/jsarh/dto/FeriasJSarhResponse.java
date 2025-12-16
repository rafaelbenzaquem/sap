package br.jus.trf1.sipe.ausencia.ferias.infrastructure.jsarh.dto;

import br.jus.trf1.sipe.ausencia.ferias.domain.model.Ferias;
import br.jus.trf1.sipe.ausencia.ferias.domain.model.Ocorrencia;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FeriasJSarhResponse(Integer id,
                                  @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate inicio,
                                  @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate fim,
                                  @JsonProperty(value = "dias_gozados") Integer diasGozados,
                                  @JsonProperty(value = "data_suspensao")
                                  @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
                                  LocalDateTime dataSuspensao,
                                  String flag) {

    public Ferias toModel() {
        return Ferias.builder()
                .id("ferias:" + id)
                .inicio(inicio)
                .fim(fim)
                .flag(Ocorrencia.parse(flag))
                .diasGozados(diasGozados)
                .dataSuspensao(dataSuspensao)
                .build();
    }
}
