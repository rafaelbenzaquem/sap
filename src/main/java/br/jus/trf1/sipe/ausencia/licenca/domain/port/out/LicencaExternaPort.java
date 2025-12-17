package br.jus.trf1.sipe.ausencia.licenca.domain.port.out;

import br.jus.trf1.sipe.ausencia.licenca.domain.model.Licenca;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LicencaExternaPort {


    List<Licenca> buscaLicencaServidorPorPeriodo(String matricula,
                                                 LocalDate inicio,
                                                 LocalDate fim);


    Optional<Licenca> buscaLicencaServidorNoDia(
            String matricula,
            LocalDate dia);

}
