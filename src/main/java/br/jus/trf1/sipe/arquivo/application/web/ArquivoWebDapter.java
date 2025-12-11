package br.jus.trf1.sipe.arquivo.application.web;

import br.jus.trf1.sipe.arquivo.application.web.dto.ArquivoWebPort;
import br.jus.trf1.sipe.arquivo.domain.port.out.ArquivoRepositoryPort;
import br.jus.trf1.sipe.arquivo.application.web.dto.ArquivoAtualizadoRequest;
import br.jus.trf1.sipe.arquivo.application.web.dto.ArquivoMetadataResponse;
import br.jus.trf1.sipe.arquivo.application.web.dto.ArquivoNovoRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import br.jus.trf1.sipe.arquivo.application.web.dto.ArquivoListResponse;
import br.jus.trf1.sipe.arquivo.application.web.dto.ArquivoResponse;

@Service
public class ArquivoWebDapter implements ArquivoWebPort {

    private final ArquivoRepositoryPort arquivoRepository;

    public ArquivoWebDapter(ArquivoRepositoryPort arquivoRepository) {
        this.arquivoRepository = arquivoRepository;
    }

    @Override
    public ArquivoMetadataResponse armazena(ArquivoNovoRequest arquivoNovoRequest) {
        return arquivoRepository.armazena(arquivoNovoRequest);
    }

    @Override
    public ArquivoMetadataResponse atualiza(ArquivoAtualizadoRequest arquivoAtualizadoRequest) {
        return arquivoRepository.atualiza(arquivoAtualizadoRequest);
    }
    
    @Override
    public Page<ArquivoListResponse> lista(int pagina, int tamanho) {
        return arquivoRepository.lista(pagina, tamanho);
    }
    
    @Override
    public ArquivoResponse recuperaPorId(String id) {
        return arquivoRepository.recuperaPorId(id);
    }
    
    @Override
    public ArquivoResponse recuperaPorNome(String nome) {
        return arquivoRepository.recuperaPorNome(nome);
    }
    
    @Override
    public ArquivoMetadataResponse recuperaMetadataPorId(String id) {
        return arquivoRepository.recuperaMetadataPorId(id);
    }
    
    @Override
    public ArquivoMetadataResponse recuperaMetadataPorNome(String nome) {
        return arquivoRepository.recuperaMetadataPorNome(nome);
    }
    
    @Override
    public ArquivoMetadataResponse apagaPorId(String id) {
        return arquivoRepository.apagaPorId(id);
    }
    
    @Override
    public ArquivoMetadataResponse apagaPorNome(String nome) {
        return arquivoRepository.apagaPorNome(nome);
    }
}
