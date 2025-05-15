package br.jus.trf1.sipe.externo.jsarh.ausencias.especial.dto;

import br.jus.trf1.sipe.externo.jsarh.ausencias.especial.FrequenciaEspecialExterno;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record FrequencialEspecialExternoResponse(
        @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate inicio,
        @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate fim,
        String descricao, String sei) {

    public FrequenciaEspecialExterno toModel() {
        return new FrequenciaEspecialExterno(inicio, fim, descricao, sei);
    }
}
