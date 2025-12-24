package br.jus.trf1.sipe.arquivo.domain.service;

import br.jus.trf1.sipe.arquivo.domain.model.Arquivo;
import br.jus.trf1.sipe.arquivo.domain.port.out.ArquivoPersistencePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArquivoServicePort implements br.jus.trf1.sipe.arquivo.domain.port.in.ArquivoServicePort {

    private final ArquivoPersistencePort arquivoRepository;

    public ArquivoServicePort(ArquivoPersistencePort arquivoRepository) {
        this.arquivoRepository = arquivoRepository;
    }

    @Override
    public Arquivo armazena(Arquivo arquivo) {
        return arquivoRepository.armazena(arquivo);
    }

    @Override
    public Arquivo atualiza(Arquivo arquivo) {
        return arquivoRepository.atualiza(arquivo);
    }

    @Override
    public List<Arquivo> lista(int pagina, int tamanho) {
        return arquivoRepository.lista(pagina, tamanho);
    }

    @Override
    public Arquivo recuperaPorId(String id) {
        return arquivoRepository.recuperaPorId(id);
    }

    @Override
    public Arquivo recuperaPorNome(String nome) {
         return arquivoRepository.recuperaPorNome(nome);
    }


    @Override
    public Arquivo apagaPorId(String id) {
        return arquivoRepository.apagaPorId(id);
    }

    @Override
    public Arquivo apagaPorNome(String nome) {
        return arquivoRepository.apagaPorNome(nome);
    }
}
