package br.jus.trf1.sipe.registro.domain.port.out;

import br.jus.trf1.sipe.registro.domain.model.Registro;

import java.time.LocalDate;
import java.util.List;

public interface RegistroExternoPort {

    List<Registro> buscaRegistrosDoDiaPorCracha(LocalDate dia, Integer cracha);

}
