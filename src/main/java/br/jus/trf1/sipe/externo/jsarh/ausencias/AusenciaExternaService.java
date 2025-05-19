package br.jus.trf1.sipe.externo.jsarh.ausencias;

import br.jus.trf1.sipe.externo.jsarh.ausencias.especial.FrequenciaEspecialExternaClient;
import br.jus.trf1.sipe.externo.jsarh.ausencias.especial.dto.FrequencialEspecialExternaResponse;
import br.jus.trf1.sipe.externo.jsarh.ausencias.ferias.FeriasExternasClient;
import br.jus.trf1.sipe.externo.jsarh.ausencias.ferias.dto.FeriasExternasResponse;
import br.jus.trf1.sipe.externo.jsarh.ausencias.licenca.LicencaExternaClient;
import br.jus.trf1.sipe.externo.jsarh.ausencias.licenca.dto.LicencaExternoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AusenciaExternaService {

    private final FrequenciaEspecialExternaClient frequenciaEspecialExternaClient;
    private final FeriasExternasClient feriasExternalService;
    private final LicencaExternaClient licencasExternalService;

    public AusenciaExternaService(FrequenciaEspecialExternaClient frequenciaEspecialExternaClient, FeriasExternasClient feriasExternalService, LicencaExternaClient licencasExternalService) {
        this.frequenciaEspecialExternaClient = frequenciaEspecialExternaClient;
        this.feriasExternalService = feriasExternalService;
        this.licencasExternalService = licencasExternalService;
    }

    public List<AusenciaExterna> buscaAusenciasServidorPorPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        log.info("Consultando licenças, férias e ausências especiais do servidor no SARH...");
        var licencas = licencasExternalService.buscaLicencaServidorPorPeriodo(matricula, inicio, fim).
                stream().map(LicencaExternoResponse::toModel).toList();

        var especiais = frequenciaEspecialExternaClient.buscaAusenciasEspeciaisServidorPorPeriodo(matricula, inicio, fim).
                stream().map(FrequencialEspecialExternaResponse::toModel).toList();

        var ferias = feriasExternalService.buscaFeriasServidorPorPeriodo(matricula.toUpperCase(), inicio, fim).
                stream().map(FeriasExternasResponse::toModel).toList();

        var ausencias = new ArrayList<AusenciaExterna>(licencas);
        ausencias.addAll(especiais);
        ausencias.addAll(ferias);

        log.info("Ausencias encontradas no período: {}", ausencias.size());
        return ausencias;
    }

    public Optional<AusenciaExterna> buscaAusenciaServidorNoDia(String matricula, LocalDate dia) {
        log.info("Consultando licenças, férias e ausências especiais do servidor no SARH...");

        var ferias = feriasExternalService.buscaFeriasServidorNoDia(matricula, dia).
                map(FeriasExternasResponse::toModel);
        if (ferias.isPresent()) {
            log.info("Possui férias - férias possui prioridade 1");
            return Optional.of(ferias.get());
        }
        var licenca = licencasExternalService.buscaLicencaServidorNoDia(matricula, dia)
                .map(LicencaExternoResponse::toModel);
        if (licenca.isPresent()) {
            log.info("Possui licença - licença possui prioridade 2");
            return Optional.of(licenca.get());
        }
        log.info("Possui ausência especial ou não possui ausência no dia");
        return frequenciaEspecialExternaClient.buscaAusenciaEspecialServidorNoDia(matricula, dia).
                map(FrequencialEspecialExternaResponse::toModel);
    }

}
