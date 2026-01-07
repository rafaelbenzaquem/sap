package br.jus.trf1.sipe.feriado.domain.service;

import br.jus.trf1.sipe.feriado.domain.model.Feriado;
import br.jus.trf1.sipe.feriado.domain.port.in.FeriadoServicePort;
import br.jus.trf1.sipe.feriado.domain.port.out.FeriadoExternoPort;
import br.jus.trf1.sipe.feriado.domain.port.out.FeriadoPersistencePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FeriadoServiceAdapter implements FeriadoServicePort {

    private final FeriadoExternoPort feriadoExternoPort;
    private final FeriadoPersistencePort feriadoPersistencePort;

    public FeriadoServiceAdapter(FeriadoExternoPort feriadoExternoPort,
                                 FeriadoPersistencePort feriadoPersistencePort) {
        this.feriadoExternoPort = feriadoExternoPort;
        this.feriadoPersistencePort = feriadoPersistencePort;
    }

    @Override
    public List<Feriado> listaPorAno(int ano) {
        return feriadoPersistencePort.listaPorAno(ano);
    }

    @Override
    public List<Feriado> listaPorPeriodo(LocalDate inicio, LocalDate fim) {
        return feriadoPersistencePort.listaPorPeriodo(inicio, fim);
    }

    @Override
    public Optional<Feriado> buscaFeriadoDoDia(LocalDate dia) {
        return feriadoPersistencePort.busca(dia);
    }

    @Override
    public List<Feriado> persisteFeriadosDoAno(int ano){
        List<Feriado> feriadosNovos = feriadoExternoPort.listaPorAno(ano);
        List<Feriado> feriadosAtuais = feriadoPersistencePort.listaPorAno(ano);
        if(feriadosNovos.equals(feriadosAtuais)){
         log.info("Não existe atualização na lista de feriados");
            return feriadosAtuais;
        }
        return feriadoPersistencePort.substitui(feriadosAtuais,feriadosNovos);
    }
}
