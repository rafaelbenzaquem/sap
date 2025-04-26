package br.jus.trf1.sipe.arquivo;

import br.jus.trf1.sipe.arquivo.web.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ArquivoHandler {

    ArquivoMetadataResponse armazena(ArquivoNovoRequest arquivo);

    ArquivoMetadataResponse atualiza(ArquivoAtualizadoRequest arquivo);

    Page<ArquivoListResponse> lista(int pag, int quantidade);

    ArquivoResponse recuperaPorId(String id);

    ArquivoResponse recuperaPorNome(String nome);

    ArquivoMetadataResponse recuperaMetadataPorId(String id);

    ArquivoMetadataResponse recuperaMetadataPorNome(String nome);

    ArquivoMetadataResponse apagaPorId(String id);

    ArquivoMetadataResponse apagaPorNome(String nome);

}
