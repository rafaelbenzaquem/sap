package br.jus.trf1.sipe.arquivo.db;

import br.jus.trf1.sipe.arquivo.exceptions.ArquivoInexistenteException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static br.jus.trf1.sipe.comum.util.PadroesDeMensagem.MSG_ENTIDADE_INEXISTENTE;

@Service
public class ArquivoCacheDBService {
    private final ArquivoRepository repository;

    public ArquivoCacheDBService(ArquivoRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "arquivosPorNome", key = "#nome")
    public Arquivo findByNome(String nome) {
        return repository.findByNome(nome)
                .orElseThrow(() ->
                        new ArquivoInexistenteException(
                                MSG_ENTIDADE_INEXISTENTE.formatted("Arquivo", nome)));
    }

    // Invalidações típicas ao atualizar/remover
    @CacheEvict(value = "arquivosPorNome", key = "#nome")
    public void evictPorNome(String nome) { }

    @CacheEvict(value = "arquivosPorNome", allEntries = true)
    public void evictTudo() { }
}
