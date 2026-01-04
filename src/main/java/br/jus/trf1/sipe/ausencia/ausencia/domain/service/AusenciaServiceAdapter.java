package br.jus.trf1.sipe.ausencia.ausencia.domain.service;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;
import br.jus.trf1.sipe.ausencia.ausencia.domain.port.in.AusenciaServicePort;
import br.jus.trf1.sipe.ausencia.ausencia.domain.port.out.AusenciaExternaPort;
import br.jus.trf1.sipe.ausencia.ausencia.domain.port.out.AusenciaPersistencePort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AusenciaServiceAdapter implements AusenciaServicePort {

    private final AusenciaExternaPort ausenciaExternaPort;
    private final AusenciaPersistencePort ausenciaPersistencePort;

    public AusenciaServiceAdapter(AusenciaExternaPort ausenciaExternaPort, AusenciaPersistencePort ausenciaPersistencePort) {
        this.ausenciaExternaPort = ausenciaExternaPort;
        this.ausenciaPersistencePort = ausenciaPersistencePort;
    }


    @Override
    public List<Ausencia> atualizaNoPeriodo(String matricula, LocalDate dataInicio, LocalDate dataFim) {
        var ausenciasNovas = ausenciaExternaPort.listaPorPeriodo(matricula, dataInicio, dataFim);
        var ausenciasAntigas = ausenciaPersistencePort.listaPorPeriodo(matricula, dataInicio, dataFim);

        //Se não tem ausencias retorna lista vazia
        if (ausenciasNovas.isEmpty() && ausenciasAntigas.isEmpty())
            return List.of();
            //Se não tem ausencias no banco apenas salva as novas
        else if (ausenciasAntigas.isEmpty())
            return ausenciaPersistencePort.salvaTodos(ausenciasNovas);
        else
            // caso contrário "replace all" substituindo as antigas pelas novas
            return ausenciaPersistencePort.substituiTodos(ausenciasAntigas, ausenciasNovas);
    }

    @Override
    public Optional<Ausencia> buscaNoDia(String matricula, LocalDate dia) {
        var ausenciaNovaOpt = ausenciaExternaPort.buscaNoDia(matricula, dia);
        var ausenciaAntigaOpt = ausenciaPersistencePort.buscaNoDia(matricula, dia);
        //Se não tem ausências retorna lista vazia
        if (ausenciaNovaOpt.isEmpty() && ausenciaAntigaOpt.isEmpty())
            return Optional.empty();
            // faz "replace" substituindo a ausência interna pela externa
            // caso contrário, se não tem ausência no banco interno apenas salva a externa
        else
            return ausenciaAntigaOpt.map(ausencia -> ausenciaPersistencePort.substitui(ausencia,
                    ausenciaNovaOpt.orElseThrow(
                            () -> new IllegalArgumentException("Não existe ausencia nova para subistituir!")
                    )
            )).or(() -> Optional.of(ausenciaPersistencePort.salva(ausenciaNovaOpt.get())));
    }
}
