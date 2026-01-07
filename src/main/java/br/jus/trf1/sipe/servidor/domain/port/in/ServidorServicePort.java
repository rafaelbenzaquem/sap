package br.jus.trf1.sipe.servidor.domain.port.in;

import br.jus.trf1.sipe.servidor.domain.model.Servidor;

import java.time.LocalDate;
import java.util.List;

public interface ServidorServicePort {

    Servidor atualizaDadosDoSarh(String matricula);
    Servidor atualizaDadosDoSarh(Servidor servidor);

    Servidor servidorAtual();

    Servidor buscaPorMatricula(String matricula);

    List<Servidor> listar();

    List<Servidor> listar(Integer idLotacaoPai);

    List<Servidor> listar(String nome, Integer cracha, String matricula, Integer idLotacao);

    List<Servidor> paginar(String nome, Integer cracha, String matricula,int page, int size);

    List<Servidor> paginar(int page, int size);

    Servidor atualizaAusenciasServidorNoPeriodo(Servidor servidor, LocalDate dataInicio, LocalDate dataFim);

    Servidor buscaDiretorLotacao(Integer idLotacao);
}