package br.jus.trf1.sipe.ponto.exceptions;

import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.ponto.PontoId;
import br.jus.trf1.sipe.usuario.infrastructure.db.UsuarioJpa;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

import static br.jus.trf1.sipe.comum.util.PadroesDeMensagem.MSG_ENTIDADE_EXISTENTE;


@Slf4j
public class PontoExistenteException extends RuntimeException {

    public PontoExistenteException(String matricula, LocalDate dia) {
        this(MSG_ENTIDADE_EXISTENTE.formatted(Ponto.class.getSimpleName(), PontoId.builder()
                .usuarioJPA(UsuarioJpa.builder()
                        .matricula(matricula)
                        .build()).
                dia(dia).
                build().
                toString()));
    }

    public PontoExistenteException(String message) {
        super(message);
        log.warn(message);
    }
}
