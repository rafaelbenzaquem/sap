package br.jus.trf1.sipe.arquivo.domain.port.in;

import br.jus.trf1.sipe.arquivo.domain.model.Arquivo;

import java.util.List;

public interface ArquivoServicePort {

    Arquivo armazena(Arquivo arquivo);

    Arquivo atualiza(Arquivo arquivo);

    List<Arquivo> lista(int pagina, int tamanho);

    Arquivo recuperaPorId(String id);

    Arquivo recuperaPorNome(String nome);

    Arquivo apagaPorId(String id);

    Arquivo apagaPorNome(String nome);

}
