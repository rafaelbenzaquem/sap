package br.jus.trf1.sipe.arquivo;

import br.jus.trf1.sipe.arquivo.web.dto.ArquivoAtualizadoRequest;
import br.jus.trf1.sipe.arquivo.web.dto.ArquivoMetadataResponse;
import br.jus.trf1.sipe.arquivo.web.dto.ArquivoNovoRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import br.jus.trf1.sipe.arquivo.web.dto.ArquivoListResponse;
import br.jus.trf1.sipe.arquivo.web.dto.ArquivoResponse;

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
    
    public Page<ArquivoListResponse> lista(int pagina, int tamanho) {
        return arquivoHandler.lista(pagina, tamanho);
    }
    
    public ArquivoResponse recuperaPorId(String id) {
        return arquivoHandler.recuperaPorId(id);
    }
    
    public ArquivoResponse recuperaPorNome(String nome) {
        return arquivoHandler.recuperaPorNome(nome);
    }
    
    public ArquivoMetadataResponse recuperaMetadataPorId(String id) {
        return arquivoHandler.recuperaMetadataPorId(id);
    }
    
    public ArquivoMetadataResponse recuperaMetadataPorNome(String nome) {
        return arquivoHandler.recuperaMetadataPorNome(nome);
    }
    
    public ArquivoMetadataResponse apagaPorId(String id) {
        return arquivoHandler.apagaPorId(id);
    }
    
    public ArquivoMetadataResponse apagaPorNome(String nome) {
        return arquivoHandler.apagaPorNome(nome);
    }
}
