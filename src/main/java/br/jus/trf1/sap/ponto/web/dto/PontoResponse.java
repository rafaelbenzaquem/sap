package br.jus.trf1.sap.ponto.web.dto;

import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.registro.web.RegistroResponse;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

import static br.jus.trf1.sap.comum.util.ConstantesDataTempoUtil.PADRAO_SAIDA_DATA;

public record PontoResponse(String matricula,
                            @JsonFormat(pattern = PADRAO_SAIDA_DATA, shape = JsonFormat.Shape.STRING)
                            LocalDate dia,
                            String descricao,
                            Float indice,
                            List<RegistroResponse> registros) {
    public static PontoResponse of(Ponto ponto) {
        return new PontoResponse(ponto.getId().getMatricula(), ponto.getId().getDia(),
                ponto.getDescricao(),ponto.getIndice().getValor(), ponto.getRegistros() == null ? null :
                ponto.getRegistros().stream().map(RegistroResponse::of).toList());
    }
}
