package br.jus.trf1.sipe.folha.web.dto;

import br.jus.trf1.sipe.folha.FechamentoFolha;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.hateoas.server.core.Relation;
import java.time.LocalDateTime;
import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_SAIDA_DATA;
import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_SAIDA_TEMPO;

/**
 * DTO de resposta para o fechamento de folha.
 */
@Relation(collectionRelation = "fechamentos")
public record FechamentoFolhaResponse(
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
    public static FechamentoFolhaResponse of(FechamentoFolha f) {
        return new FechamentoFolhaResponse(
                f.getId(),
                f.getServidor().getMatricula(),
                br.jus.trf1.sipe.folha.Mes.getMes(f.getMes()).getNome(),
                f.getAno(),
                f.getHorasExecutadasMinutos(),
                f.getHorasEsperadasMinutos(),
                f.getSaldoMinutos(),
                f.getDataFechamento().toLocalDateTime(),
                f.getPrazoCompensacao().toLocalDateTime()
        );
    }
}