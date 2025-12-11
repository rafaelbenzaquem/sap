package br.jus.trf1.sipe.arquivo.domain.port.out;

import br.jus.trf1.sipe.arquivo.domain.model.Arquivo;

public interface ArquivoCachePort {

    Arquivo findByNome(String nome);

    void evictPorNome(String nome);

    void evictTudo();
}
