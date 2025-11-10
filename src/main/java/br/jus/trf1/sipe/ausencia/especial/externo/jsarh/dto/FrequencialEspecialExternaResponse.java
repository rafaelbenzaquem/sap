package br.jus.trf1.sipe.ausencia.especial.externo.jsarh.dto;

import br.jus.trf1.sipe.ausencia.especial.externo.jsarh.FrequenciaEspecialExterna;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record FrequencialEspecialExternaResponse(
        Integer id,
        @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate inicio,
        @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate fim,
        String descricao, String sei) {

    public FrequenciaEspecialExterna toModel() {
        return new FrequenciaEspecialExterna("especial:" + id, inicio, fim, descricao, sei);
    }
}
