package br.jus.trf1.sipe.folha.application.web.dto;

import br.jus.trf1.sipe.folha.domain.model.Folha;
import lombok.Builder;

@Builder
public record FolhaResponse(String matricula,
                            String mes,
                            Integer ano,
                            Boolean homologado) {

    public static FolhaResponse of(Folha folha) {
        return FolhaResponse.builder()
                .matricula(folha.getId().getServidor().getMatricula())
                .mes(folha.getId().getMes().getNome())
                .ano(folha.getId().getAno())
                .homologado(folha.getDataHomologacao() != null)
                .build();
    }
}
