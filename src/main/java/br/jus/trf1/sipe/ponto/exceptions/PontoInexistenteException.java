package br.jus.trf1.sipe.ponto.exceptions;

import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.ponto.PontoId;
import br.jus.trf1.sipe.usuario.Usuario;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

import static br.jus.trf1.sipe.comum.util.PadroesDeMensagem.MSG_ENTIDADE_INEXISTENTE;

@Slf4j
public class PontoInexistenteException extends RuntimeException {


    public PontoInexistenteException(String matricula, LocalDate dia) {
        this(MSG_ENTIDADE_INEXISTENTE.formatted(Ponto.class.getSimpleName(), PontoId.builder().
                usuario(Usuario.builder()
                        .matricula(matricula)
                        .build()).
                dia(dia).
                build().
                toString()));
    }

    public PontoInexistenteException(String message) {
        super(message);
        log.warn(message);
    }
}
