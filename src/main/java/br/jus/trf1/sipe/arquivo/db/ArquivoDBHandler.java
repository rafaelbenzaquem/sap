package br.jus.trf1.sipe.arquivo.db;

import br.jus.trf1.sipe.arquivo.ArquivoHandler;
import br.jus.trf1.sipe.arquivo.exceptions.ArquivoInexistenteException;
import br.jus.trf1.sipe.arquivo.web.dto.*;
import br.jus.trf1.sipe.comum.exceptions.CamposUnicosExistentesException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static br.jus.trf1.sipe.comum.util.PadroesDeMensagem.MSG_ENTIDADE_INEXISTENTE;

@Service
public class ArquivoDBHandler implements ArquivoHandler {

    private final ArquivoRepository repository;
    private final ArquivoCacheDBService cache;

    public ArquivoDBHandler(ArquivoRepository repository,
                            ArquivoCacheDBService cache) {
        this.repository = repository;
        this.cache = cache;
    }

    @Override
    public ArquivoMetadataResponse armazena(ArquivoNovoRequest arquivo) {
        if (repository.checaSeExisteArquivoComNome(arquivo.nome(), arquivo.id())) {
            var map = new HashMap<String, String>();
            map.put("nome", "já existe arquivo com nome " + arquivo.nome());
            throw new CamposUnicosExistentesException(map);
        }
        Arquivo arquivoSalvo = repository.save(arquivo.toModel());
        cache.evictPorNome(arquivo.nome());
        return ArquivoMetadataResponse.of(arquivoSalvo);
    }

    @Override
    public ArquivoMetadataResponse atualiza(ArquivoAtualizadoRequest arquivo) {
        if (repository.existsById(arquivo.id())) {
            if (repository.checaSeExisteArquivoComNome(arquivo.nome(), arquivo.id())) {
                var map = new HashMap<String, String>();
                map.put("nome", "já existe arquivo com nome " + arquivo.nome());
                throw new CamposUnicosExistentesException(map);
            }

            Arquivo arquivoSalvo = repository.save(arquivo.toModel());
            cache.evictPorNome(arquivo.nome());
            return ArquivoMetadataResponse.of(arquivoSalvo);
        }
        throw new ArquivoInexistenteException(MSG_ENTIDADE_INEXISTENTE.formatted("Arquivo", arquivo.id()));
    }

    @Override
    public Page<ArquivoListResponse> lista(int pag, int tamanho) {
        return repository.findAll(PageRequest.of(pag, tamanho)).map(ArquivoListResponse::of);
    }

    @Override
    public ArquivoResponse recuperaPorId(String id) {
        var opt = repository.findById(id);
        if (opt.isPresent()) {
            return ArquivoResponse.of(opt.get());
        }
        throw new ArquivoInexistenteException(MSG_ENTIDADE_INEXISTENTE.formatted("Arquivo", id));
    }

    @Override
    public ArquivoResponse recuperaPorNome(String nome) {
        return ArquivoResponse.of(cache.findByNome(nome));
    }

    @Override
    public ArquivoMetadataResponse recuperaMetadataPorId(String id) {
        var opt = repository.findById(id);
        if (opt.isPresent()) {
            return ArquivoMetadataResponse.of(opt.get());
        }
        throw new ArquivoInexistenteException(MSG_ENTIDADE_INEXISTENTE.formatted("Arquivo", id));
    }

    @Override
    public ArquivoMetadataResponse recuperaMetadataPorNome(String nome) {
        var opt = repository.findByNome(nome);
        if (opt.isPresent()) {
            return ArquivoMetadataResponse.of(opt.get());
        }
        throw new ArquivoInexistenteException(MSG_ENTIDADE_INEXISTENTE.formatted("Arquivo", nome));
    }

    @Override
    public ArquivoMetadataResponse apagaPorId(String id) {
        var opt = repository.findById(id);
        if (opt.isPresent()) {
            repository.delete(opt.get());
            return ArquivoMetadataResponse.of(opt.get());
        }
        throw new ArquivoInexistenteException(MSG_ENTIDADE_INEXISTENTE.formatted("Arquivo", id));
    }

    @Override
    public ArquivoMetadataResponse apagaPorNome(String nome) {
        var opt = repository.findByNome(nome);
        if (opt.isPresent()) {
            repository.delete(opt.get());
            cache.evictPorNome(nome);
            return ArquivoMetadataResponse.of(opt.get());
        }
        throw new ArquivoInexistenteException(MSG_ENTIDADE_INEXISTENTE.formatted("Arquivo", nome));
    }
}
