package br.jus.trf1.sipe.ausencia.especial.infrastructure.jsarh;

import br.jus.trf1.sipe.ausencia.especial.domain.model.Especial;
import br.jus.trf1.sipe.ausencia.especial.domain.port.out.EspecialExternaPort;
import br.jus.trf1.sipe.ausencia.especial.infrastructure.jsarh.dto.EspecialJSarhResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EspecialJSarhAdapter implements EspecialExternaPort {

    private final EspecialJSarhClient especialJSarhClient;


    public EspecialJSarhAdapter(EspecialJSarhClient especialJSarhClient) {
        this.especialJSarhClient = especialJSarhClient;
    }

    @Override
    public List<Especial> buscaAusenciasEspeciaisServidorPorPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        return especialJSarhClient.buscaAusenciasEspeciaisServidorPorPeriodo(matricula, inicio, fim).stream().map(EspecialJSarhResponse::toModel).toList();
    }

    @Override
    public Optional<Especial> buscaAusenciaEspecialServidorNoDia(String matricula, LocalDate dia) {
        return Optional.empty();
    }
}
