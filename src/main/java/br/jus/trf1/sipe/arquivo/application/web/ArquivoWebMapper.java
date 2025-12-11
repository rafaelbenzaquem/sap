package br.jus.trf1.sipe.arquivo.application.web;

import br.jus.trf1.sipe.arquivo.application.web.dto.*;
import br.jus.trf1.sipe.arquivo.domain.model.Arquivo;
import br.jus.trf1.sipe.arquivo.infrastructure.db.ArquivoJpa;

public class ArquivoWebMapper {

    private ArquivoWebMapper() {
        // Esconda o construtor público para garantir que a classe não seja instanciada
    }

    public Arquivo toDomain(ArquivoAtualizadoRequest request) {
        return Arquivo.builder()
                .id(request.id())
                .nome(request.nome())
                .tipoDeConteudo(request.tipoDeConteudo())
                .bytes(request.bytes())
                .descricao(request.descricao())
                .build();
    }

    public static ArquivoResponse toResponse(Arquivo arquivo) {
        return ArquivoResponse.builder()
                .id(arquivo.getId())
                .nome(arquivo.getNome())
                .tipoDeConteudo(arquivo.getTipoDeConteudo())
                .bytes(arquivo.getBytes())
                .descricao(arquivo.getDescricao())
                .build();
    }

    public static ArquivoListResponse toListResponse(Arquivo arquivo) {
        return new ArquivoListResponse(arquivo.getId(), arquivo.getNome());
    }

    public static ArquivoMetadataResponse toMetadataResponse(ArquivoJpa arquivo){
        return ArquivoMetadataResponse.builder()
                .id(arquivo.getId())
                .nome(arquivo.getNome())
                .tipoDeConteudo(arquivo.getTipoDeConteudo())
                .descricao(arquivo.getDescricao())
                .tamanho(arquivo.getBytes().length*8L)
                .build();
    }

    public ArquivoJpa toDomain(ArquivoNovoRequest request) {
        return ArquivoJpa.builder()
                .id(request.id())
                .nome(request.nome())
                .tipoDeConteudo(request.tipoDeConteudo())
                .bytes(request.bytes())
                .descricao(request.descricao())
                .build();
    }
}
