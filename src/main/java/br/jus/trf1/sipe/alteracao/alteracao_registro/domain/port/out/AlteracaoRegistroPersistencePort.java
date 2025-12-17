package br.jus.trf1.sipe.alteracao.alteracao_registro.domain.port.out;

import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.AlteracaoRegistro;

import java.util.Optional;

public interface AlteracaoRegistroPersistencePort {

    AlteracaoRegistro salva(AlteracaoRegistro alteracaoRegistro);

    Optional<AlteracaoRegistro> buscaPorId(Long id);

    void apaga(AlteracaoRegistro alteracaoRegistro);

}
