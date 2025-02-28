package br.jus.trf1.sap.externo.jsarh.ausencias.especial.dto;

import br.jus.trf1.sap.externo.jsarh.ausencias.especial.Especial;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record EspecialResponse(
        @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate inicio,
        @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate fim,
        String descricao, String sei) {

    public Especial toModel() {
        return new Especial(inicio, fim, descricao, sei);
    }
}
