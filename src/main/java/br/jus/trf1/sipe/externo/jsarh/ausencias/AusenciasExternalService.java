package br.jus.trf1.sipe.externo.jsarh.ausencias;

import br.jus.trf1.sipe.externo.jsarh.ausencias.especial.FrequenciaEspecialClient;
import br.jus.trf1.sipe.externo.jsarh.ausencias.especial.dto.FrequencialEspecialExternoResponse;
import br.jus.trf1.sipe.externo.jsarh.ausencias.ferias.FeriasExternalClient;
import br.jus.trf1.sipe.externo.jsarh.ausencias.ferias.dto.FeriasExternalResponse;
import br.jus.trf1.sipe.externo.jsarh.ausencias.licenca.LicencaExternalClient;
import br.jus.trf1.sipe.externo.jsarh.ausencias.licenca.dto.LicencaExternalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AusenciasExternalService {

    private final FrequenciaEspecialClient frequenciaEspecialClient;
    private final FeriasExternalClient feriasExternalService;
    private final LicencaExternalClient licencasExternalService;

    public AusenciasExternalService(FrequenciaEspecialClient frequenciaEspecialClient, FeriasExternalClient feriasExternalService, LicencaExternalClient licencasExternalService) {
        this.frequenciaEspecialClient = frequenciaEspecialClient;
        this.feriasExternalService = feriasExternalService;
        this.licencasExternalService = licencasExternalService;
    }

    public List<AusenciaExternal> buscaAusenciasServidorPorPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        log.info("Consultando licenças, férias e ausências especiais do servidor no SARH...");
        var licencas = licencasExternalService.buscaLicencaServidorPorPeriodo(matricula, inicio, fim).
                stream().map(LicencaExternalResponse::toModel).toList();

        var especiais = frequenciaEspecialClient.buscaAusenciasEspeciaisServidorPorPeriodo(matricula, inicio, fim).
                stream().map(FrequencialEspecialExternoResponse::toModel).toList();

        var ferias = feriasExternalService.buscaFeriasServidorPorPeriodo(matricula.toUpperCase(), inicio, fim).
                stream().map(FeriasExternalResponse::toModel).toList();

        var ausencias = new ArrayList<AusenciaExternal>(licencas);
        ausencias.addAll(especiais);
        ausencias.addAll(ferias);

        log.info("Ausencias encontradas no período: {}", ausencias.size());
        return ausencias;
    }

    public Optional<AusenciaExternal> buscaAusenciaServidorNoDia(String matricula, LocalDate dia) {
        log.info("Consultando licenças, férias e ausências especiais do servidor no SARH...");

        var ferias = feriasExternalService.buscaFeriasServidorNoDia(matricula, dia).
                map(FeriasExternalResponse::toModel);
        if (ferias.isPresent()) {
            log.info("Possui férias - férias possui prioridade 1");
            return Optional.of(ferias.get());
        }
        var licenca = licencasExternalService.buscaLicencaServidorNoDia(matricula, dia)
                .map(LicencaExternalResponse::toModel);
        if (licenca.isPresent()) {
            log.info("Possui licença - licença possui prioridade 2");
            return Optional.of(licenca.get());
        }
        log.info("Possui ausência especial ou não possui ausência no dia");
        return frequenciaEspecialClient.buscaAusenciaEspecialServidorNoDia(matricula, dia).
                map(FrequencialEspecialExternoResponse::toModel);
    }

}
