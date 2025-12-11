package br.jus.trf1.sipe.arquivo.application.web.dto;

import org.springframework.data.domain.Page;

public interface ArquivoWebPort {
    ArquivoMetadataResponse armazena(ArquivoNovoRequest arquivoNovoRequest);

    ArquivoMetadataResponse atualiza(ArquivoAtualizadoRequest arquivoAtualizadoRequest);

    Page<ArquivoListResponse> lista(int pagina, int tamanho);

    ArquivoResponse recuperaPorId(String id);

    ArquivoResponse recuperaPorNome(String nome);

    ArquivoMetadataResponse recuperaMetadataPorId(String id);

    ArquivoMetadataResponse recuperaMetadataPorNome(String nome);

    ArquivoMetadataResponse apagaPorId(String id);

    ArquivoMetadataResponse apagaPorNome(String nome);
}
