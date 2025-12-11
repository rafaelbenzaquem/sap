package br.jus.trf1.sipe.arquivo.infrastructure.db;

import br.jus.trf1.sipe.arquivo.domain.exceptions.ArquivoInexistenteException;
import br.jus.trf1.sipe.arquivo.domain.model.Arquivo;
import br.jus.trf1.sipe.arquivo.domain.port.out.ArquivoCachePort;
import br.jus.trf1.sipe.arquivo.domain.port.out.ArquivoRepositoryPort;
import br.jus.trf1.sipe.comum.exceptions.CamposUnicosExistentesException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

import static br.jus.trf1.sipe.comum.util.PadroesDeMensagem.MSG_ENTIDADE_INEXISTENTE;

@Component
public class ArquivoJpaAdapter implements ArquivoRepositoryPort {

    private final ArquivoJpaRepository repository;
    private final ArquivoCachePort cache;

    public ArquivoJpaAdapter(ArquivoJpaRepository repository,
                             ArquivoCachePort cache) {
        this.repository = repository;
        this.cache = cache;
    }

    @Override
    public Arquivo armazena(Arquivo arquivo) {
        if (repository.checaSeExisteArquivoComNome(arquivo.getNome(), arquivo.getId())) {
            var map = new HashMap<String, String>();
            map.put("nome", "já existe arquivo com nome " + arquivo.getNome());
            throw new CamposUnicosExistentesException(map);
        }
        ArquivoJpa arquivoJpa = ArquivoJpaMapper.toEntity(arquivo);
        arquivoJpa = repository.save(arquivoJpa);
        cache.evictPorNome(arquivo.getNome());
        return ArquivoJpaMapper.toDomain(arquivoJpa);
    }

    @Override
    public Arquivo atualiza(Arquivo arquivo) {
        if (repository.existsById(arquivo.getId())) {
            if (repository.checaSeExisteArquivoComNome(arquivo.getNome(), arquivo.getId())) {
                var map = new HashMap<String, String>();
                map.put("nome", "já existe arquivo com nome " + arquivo.getNome());
                throw new CamposUnicosExistentesException(map);
            }

            ArquivoJpa arquivoJpa = ArquivoJpaMapper.toEntity(arquivo);
             arquivoJpa = repository.save(arquivoJpa);
            cache.evictPorNome(arquivoJpa.getNome());
            return ArquivoJpaMapper.toDomain(arquivoJpa);
        }
        throw new ArquivoInexistenteException(MSG_ENTIDADE_INEXISTENTE.formatted("Arquivo", arquivo.getId()));
    }

    @Override
    public List<Arquivo> lista(int pag, int tamanho) {
        return repository.findAll(PageRequest.of(pag, tamanho)).map(ArquivoJpaMapper::toDomain).toList();
    }

    @Override
    public Arquivo recuperaPorId(String id) {
        var opt = repository.findById(id);
        if (opt.isPresent()) {
            return ArquivoJpaMapper.toDomain(opt.get());
        }
        throw new ArquivoInexistenteException(MSG_ENTIDADE_INEXISTENTE.formatted("Arquivo", id));
    }

    @Override
    public Arquivo recuperaPorNome(String nome) {
        return ArquivoJpaMapper.toDomain(cache.findByNome(nome));
    }

    @Override
    public Arquivo apagaPorId(String id) {
        var opt = repository.findById(id);
        if (opt.isPresent()) {
            repository.delete(opt.get());
            return ArquivoJpaMapper.toDomain(opt.get());
        }
        throw new ArquivoInexistenteException(MSG_ENTIDADE_INEXISTENTE.formatted("Arquivo", id));
    }

    @Override
    public Arquivo apagaPorNome(String nome) {
        var opt = repository.findByNome(nome);
        if (opt.isPresent()) {
            repository.delete(opt.get());
            cache.evictPorNome(nome);
            return ArquivoJpaMapper.toDomain(opt.get());
        }
        throw new ArquivoInexistenteException(MSG_ENTIDADE_INEXISTENTE.formatted("Arquivo", nome));
    }

    @Override
    public long contar() {
        return repository.count();
    }
}
