package br.jus.trf1.sap.vinculo.exceptions;

import br.jus.trf1.sap.vinculo.Vinculo;

import static br.jus.trf1.sap.comum.util.ConstantesDataTempoUtil.MSG_ENTIDADE_INEXISTENTE;

public class VinculoInexistenteException extends RuntimeException {

    public VinculoInexistenteException(Integer id) {
        this(MSG_ENTIDADE_INEXISTENTE.formatted(Vinculo.class.getSimpleName(), id));
    }

    public VinculoInexistenteException(String msg) {
        super(msg);
    }

}
