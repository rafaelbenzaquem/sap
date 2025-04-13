package br.jus.trf1.sipe.externo.jsarh.ausencias;

import br.jus.trf1.sipe.externo.jsarh.ausencias.especial.EspecialService;
import br.jus.trf1.sipe.externo.jsarh.ausencias.especial.dto.EspecialResponse;
import br.jus.trf1.sipe.externo.jsarh.ausencias.ferias.FeriasService;
import br.jus.trf1.sipe.externo.jsarh.ausencias.ferias.dto.FeriasResponse;
import br.jus.trf1.sipe.externo.jsarh.ausencias.licenca.LicencasService;
import br.jus.trf1.sipe.externo.jsarh.ausencias.licenca.dto.LicencaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AusenciasService {

    private final EspecialService especialService;
    private final FeriasService feriasService;
    private final LicencasService licencasService;

    public AusenciasService(EspecialService especialService, FeriasService feriasService, LicencasService licencasService) {
        this.especialService = especialService;
        this.feriasService = feriasService;
        this.licencasService = licencasService;
    }

    public List<Ausencia> buscaAusenciasServidorPorPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        log.info("Consultando licenças, férias e ausências especiais do servidor no SARH...");
        var licencas = licencasService.buscaLicencaServidorPorPeriodo(matricula, inicio, fim).
                stream().map(LicencaResponse::toModel).toList();

        var especiais = especialService.buscaAusenciasEspeciaisServidorPorPeriodo(matricula, inicio, fim).
                stream().map(EspecialResponse::toModel).toList();

        var ferias = feriasService.buscaFeriasServidorPorPeriodo(matricula.toUpperCase(), inicio, fim).
                stream().map(FeriasResponse::toModel).toList();

        var ausencias = new ArrayList<Ausencia>(licencas);
        ausencias.addAll(especiais);
        ausencias.addAll(ferias);

        log.info("Ausencias encontradas no período: {}", ausencias.size());
        return ausencias;
    }

    public Optional<Ausencia> buscaAusenciaServidorNoDia(String matricula, LocalDate dia) {
        log.info("Consultando licenças, férias e ausências especiais do servidor no SARH...");

        var ferias = feriasService.buscaFeriasServidorNoDia(matricula, dia).
                map(FeriasResponse::toModel);
        if (ferias.isPresent()) {
            log.info("Possui férias - férias possui prioridade 1");
            return Optional.of(ferias.get());
        }
        var licenca = licencasService.buscaLicencaServidorNoDia(matricula, dia)
                .map(LicencaResponse::toModel);
        if (licenca.isPresent()) {
            log.info("Possui licença - licença possui prioridade 2");
            return Optional.of(licenca.get());
        }
        log.info("Possui ausência especial ou não possui ausência no dia");
        return especialService.buscaAusenciaEspecialServidorNoDia(matricula, dia).
                map(EspecialResponse::toModel);
    }

}
