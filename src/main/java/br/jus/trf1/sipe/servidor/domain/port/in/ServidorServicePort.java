package br.jus.trf1.sipe.servidor.domain.port.in;

import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ServidorServicePort {

    Servidor atualizaDadosNoSarh(String matricula);

    Servidor servidorAtual();

    Servidor buscaPorMatricula(String matricula);

    List<Servidor> listar();

    List<Servidor> listar(Integer idLotacaoPai);

    List<Servidor> listar(String nome, Integer cracha, String matricula, Integer idLotacao);

    Page<Servidor> paginar(Pageable pageable);

    Page<Servidor> paginar(String nome, Integer cracha, String matricula, Pageable pageable);

    Servidor vinculaAusenciasServidorNoPeriodo(Servidor servidor, LocalDate dataInicio, LocalDate dataFim);

    Servidor buscaDiretorLotacao(Integer idLotacao);

    Page<Servidor> paginar(int page, int size);
}