package br.jus.trf1.sipe.arquivo.infrastructure.cache;

import br.jus.trf1.sipe.arquivo.domain.model.Arquivo;
import br.jus.trf1.sipe.arquivo.domain.port.out.ArquivoCachePort;
import br.jus.trf1.sipe.arquivo.exceptions.ArquivoInexistenteException;
import br.jus.trf1.sipe.arquivo.infrastructure.db.ArquivoJpaMapper;
import br.jus.trf1.sipe.arquivo.infrastructure.db.ArquivoJpaRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static br.jus.trf1.sipe.comum.util.PadroesDeMensagem.MSG_ENTIDADE_INEXISTENTE;

@Service
public class ArquivoCacheJpaAdapter implements ArquivoCachePort {

    private final ArquivoJpaRepository repository;

    public ArquivoCacheJpaAdapter(ArquivoJpaRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "arquivosPorNome", key = "#nome")
    @Override
    public Arquivo findByNome(String nome) {
        return repository.findByNome(nome)
                .map(ArquivoJpaMapper::toDomain)
                .orElseThrow(() ->
                        new ArquivoInexistenteException(
                                MSG_ENTIDADE_INEXISTENTE.formatted("Arquivo", nome)));
    }

    // Invalidações típicas ao atualizar/remover
    @CacheEvict(value = "arquivosPorNome", key = "#nome")
    @Override
    public void evictPorNome(String nome) { }

    @CacheEvict(value = "arquivosPorNome", allEntries = true)
    @Override
    public void evictTudo() { }
}
