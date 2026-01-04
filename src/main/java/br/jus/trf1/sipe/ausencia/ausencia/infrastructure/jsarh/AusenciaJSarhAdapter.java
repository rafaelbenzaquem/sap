package br.jus.trf1.sipe.ausencia.ausencia.infrastructure.jsarh;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;
import br.jus.trf1.sipe.ausencia.ausencia.domain.port.out.AusenciaExternaPort;
import br.jus.trf1.sipe.ausencia.especial.domain.port.out.EspecialExternaPort;
import br.jus.trf1.sipe.ausencia.ferias.domain.port.out.FeriasExternaPort;
import br.jus.trf1.sipe.ausencia.licenca.domain.port.out.LicencaExternaPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AusenciaJSarhAdapter implements AusenciaExternaPort {

    private final EspecialExternaPort especialExternaPort;
    private final FeriasExternaPort feriasExternaPort;
    private final LicencaExternaPort licencaExternaPort;

    public AusenciaJSarhAdapter(EspecialExternaPort especialExternaPort, FeriasExternaPort feriasExternaPort, LicencaExternaPort licencaExternaPort) {
        this.especialExternaPort = especialExternaPort;
        this.feriasExternaPort = feriasExternaPort;
        this.licencaExternaPort = licencaExternaPort;
    }

    @Override
    public List<Ausencia> listaPorPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        log.info("Consultando licenças, férias e ausências especiais do servidor no SARH...");
        var licencas = licencaExternaPort.buscaLicencaServidorPorPeriodo(matricula, inicio, fim);

        var especiais = especialExternaPort.buscaAusenciasEspeciaisServidorPorPeriodo(matricula, inicio, fim);

        var ferias = feriasExternaPort.buscaFeriasServidorPorPeriodo(matricula.toUpperCase(), inicio, fim);

        var ausencias = new ArrayList<Ausencia>(licencas);
        ausencias.addAll(especiais);
        ausencias.addAll(ferias);

        log.info("Ausencias encontradas no período: {}", ausencias.size());
        return ausencias;
    }

    @Override
    public Optional<Ausencia> buscaNoDia(String matricula, LocalDate dia) {
        log.info("Consultando licença, dia de férias ou ausência especial do servidor no SARH...");

        var ferias = feriasExternaPort.buscaFeriasServidorNoDia(matricula, dia);
        if (ferias.isPresent()) {
            log.info("Possui férias - férias possui prioridade 1");
            return Optional.of(ferias.get());
        }
        var licenca = licencaExternaPort.buscaLicencaServidorNoDia(matricula, dia);
        if (licenca.isPresent()) {
            log.info("Possui licença - licença possui prioridade 2");
            return Optional.of(licenca.get());
        }
        var especial = especialExternaPort.buscaAusenciaEspecialServidorNoDia(matricula, dia);
        if (especial.isPresent()) {
            log.info("Possui ausência especial no dia");
            return Optional.of(especial.get());
        }
        log.info("Não possui ausência no dia");
        return Optional.empty();
    }

}
