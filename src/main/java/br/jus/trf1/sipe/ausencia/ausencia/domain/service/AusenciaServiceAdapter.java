package br.jus.trf1.sipe.ausencia.ausencia.domain.service;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;
import br.jus.trf1.sipe.ausencia.ausencia.domain.port.in.AusenciaServicePort;
import br.jus.trf1.sipe.ausencia.ausencia.domain.port.out.AusenciaExternaPort;
import br.jus.trf1.sipe.ausencia.ausencia.domain.port.out.AusenciaPersistencePort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AusenciaServiceAdapter implements AusenciaServicePort {

    private final AusenciaExternaPort ausenciaExternaPort;
    private final AusenciaPersistencePort ausenciaPersistencePort;

    public AusenciaServiceAdapter(AusenciaExternaPort ausenciaExternaPort, AusenciaPersistencePort ausenciaPersistencePort) {
        this.ausenciaExternaPort = ausenciaExternaPort;
        this.ausenciaPersistencePort = ausenciaPersistencePort;
    }


    @Override
    public List<Ausencia> atualizaAusencias(String matricula, LocalDate dataInicio, LocalDate dataFim) {

        var novasAusencias = ausenciaExternaPort.buscaAusenciasServidorPorPeriodo(matricula, dataInicio, dataFim);

        var ausenciasExistentes = ausenciaPersistencePort.listaAusenciasPorServidorMaisPeriodo(matricula, dataInicio, dataFim);

        var ausenciasParaDelete = ausenciasExistentes.stream().filter(ae -> !novasAusencias.contains(ae)).toList();
        var ausenciasParaSalve = novasAusencias.stream().filter(ae -> !ausenciasExistentes.contains(ae)).toList();

        if (!ausenciasParaDelete.isEmpty()) {
            ausenciaPersistencePort.deletaTodos(ausenciasParaDelete);
        }

        if (!ausenciasParaSalve.isEmpty()) {
            ausenciasParaSalve.forEach(ausenciaPersistencePort::salva);
        }

        return ausenciaPersistencePort.listaAusenciasPorServidorMaisPeriodo(matricula, dataInicio, dataFim);
    }
}
