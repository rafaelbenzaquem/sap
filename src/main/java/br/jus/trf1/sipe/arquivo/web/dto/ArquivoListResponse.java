package br.jus.trf1.sipe.arquivo.web.dto;

import br.jus.trf1.sipe.arquivo.db.Arquivo;

public record ArquivoListResponse(String id, String nome) {
    public static ArquivoListResponse of(Arquivo arquivo) {
        return new ArquivoListResponse(arquivo.getId(), arquivo.getNome());
    }
}
