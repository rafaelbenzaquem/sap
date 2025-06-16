package br.jus.trf1.sipe.folha.web.dto;

import br.jus.trf1.sipe.folha.Folha;
import br.jus.trf1.sipe.ponto.web.dto.PontoResponse;
import lombok.Builder;

import java.util.Collections;
import java.util.List;

@Builder
public record FolhaResponse(String matricula,
                            String mes,
                            Integer ano,
                            Boolean homologado,
                            List<PontoResponse> pontos) {

    public static FolhaResponse of(Folha folha) {
        return FolhaResponse.builder()
                .matricula(folha.getId().getServidor().getMatricula())
                .mes(folha.getId().getMes().getNome())
                .ano(folha.getId().getAno())
                .homologado(folha.getDataHomologacao() != null)
                .pontos(folha.getPontos() == null ? Collections.emptyList() : folha.getPontos().stream().map(PontoResponse::of).toList())
                .build();
    }
}
