package br.jus.trf1.sap.ponto.web.dto;

import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.registro.web.RegistroResponse;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

import static br.jus.trf1.sap.util.ConstantesDataTempoUtil.PADRAO_SAIDA_DATA;

public record PontoResponse(String matricula,
                            LocalDate dia,
                            String descricao,
                            @DateTimeFormat(pattern = PADRAO_SAIDA_DATA)
                            List<RegistroResponse> registros) {
    public static PontoResponse of(Ponto ponto) {
        return new PontoResponse(ponto.getId().getMatricula(), ponto.getId().getDia(),
                ponto.getDescricao(), ponto.getRegistros() == null ? null :
                ponto.getRegistros().stream().map(RegistroResponse::of).toList());
    }
}
