package br.jus.trf1.sap.ponto.web.dto;

import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.ponto.registro.web.RegistroResponse;

import java.time.LocalDate;
import java.util.List;

public record PontoResponse(Integer matricula, LocalDate dia, String descricao, List<RegistroResponse> registros) {
    public static PontoResponse of(Ponto ponto) {
        return new PontoResponse(ponto.getId().getMatricula(), ponto.getId().getDia(),
                ponto.getDescricao(), ponto.getRegistros().stream().map(RegistroResponse::of).toList());
    }
}
