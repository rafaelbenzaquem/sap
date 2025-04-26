package br.jus.trf1.sipe.arquivo;

import br.jus.trf1.sipe.arquivo.web.dto.ArquivoAtualizadoRequest;
import br.jus.trf1.sipe.arquivo.web.dto.ArquivoMetadataResponse;
import br.jus.trf1.sipe.arquivo.web.dto.ArquivoNovoRequest;
import org.springframework.stereotype.Service;

@Service
public class ArquivoService {

    ArquivoHandler arquivoHandler;

    public ArquivoService(ArquivoHandler arquivoHandler) {
        this.arquivoHandler = arquivoHandler;
    }

    public ArquivoMetadataResponse armazena(ArquivoNovoRequest arquivoNovoRequest) {
        return arquivoHandler.armazena(arquivoNovoRequest);
    }

    public ArquivoMetadataResponse atualiza(ArquivoAtualizadoRequest arquivoAtualizadoRequest) {
        return arquivoHandler.atualiza(arquivoAtualizadoRequest);
    }
}
