package br.jus.trf1.sipe.arquivo.domain.port.out;

import br.jus.trf1.sipe.arquivo.domain.model.Arquivo;

import java.util.List;

public interface ArquivoRepositoryPort {

    Arquivo armazena(Arquivo arquivo);

    Arquivo atualiza(Arquivo arquivo);

    List<Arquivo> lista(int page, int size);

    Arquivo recuperaPorId(String id);

    Arquivo recuperaPorNome(String nome);

    Arquivo apagaPorId(String id);

    Arquivo apagaPorNome(String nome);

    long contar();

}
