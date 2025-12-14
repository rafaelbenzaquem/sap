package br.jus.trf1.sipe.fechamento.application.web.dto;

import br.jus.trf1.sipe.fechamento.domain.model.Fechamento;
import br.jus.trf1.sipe.folha.domain.model.Mes;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_SAIDA_DATA;
import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_SAIDA_TEMPO;

/**
 * DTO de resposta para o fechamento de folha.
 */
@Relation(collectionRelation = "fechamentos")
public record FechamentoResponse(
        Long id,
        String matricula,
        String mes,
        Integer ano,
        Long horasExecutadasMinutos,
        Long horasEsperadasMinutos,
        Long saldoMinutos,
        @JsonFormat(pattern = PADRAO_SAIDA_DATA + " " + PADRAO_SAIDA_TEMPO, shape = JsonFormat.Shape.STRING)
        LocalDateTime dataFechamento,
        @JsonFormat(pattern = PADRAO_SAIDA_DATA + " " + PADRAO_SAIDA_TEMPO, shape = JsonFormat.Shape.STRING)
        LocalDateTime prazoCompensacao
) {
    public static FechamentoResponse of(Fechamento fechamento) {
        return new FechamentoResponse(
                fechamento.getId(),
                fechamento.getServidor().getMatricula(),
                Mes.getMes(fechamento.getMes()).getNome(),
                fechamento.getAno(),
                fechamento.getHorasExecutadasMinutos(),
                fechamento.getHorasEsperadasMinutos(),
                fechamento.getSaldoMinutos(),
                fechamento.getDataFechamento().toLocalDateTime(),
                fechamento.getPrazoCompensacao().toLocalDateTime()
        );
    }
}