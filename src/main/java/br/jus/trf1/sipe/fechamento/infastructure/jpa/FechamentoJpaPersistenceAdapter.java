package br.jus.trf1.sipe.fechamento.infastructure.jpa;

import br.jus.trf1.sipe.fechamento.domain.model.Fechamento;
import br.jus.trf1.sipe.fechamento.domain.port.out.FechamentoPersistencePort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FechamentoJpaPersistenceAdapter implements FechamentoPersistencePort {

    private final FechamentoJpaRepository fechamentoJpaRepository;

    public FechamentoJpaPersistenceAdapter(FechamentoJpaRepository fechamentoJpaRepository) {
        this.fechamentoJpaRepository = fechamentoJpaRepository;
    }

    @Override
    public Optional<Fechamento> buscaPorMatriculaMesAno(String matricula, Integer valorMes, Integer ano) {
        var fechamentoJpaOpt = fechamentoJpaRepository.findByServidorMatriculaAndMesAndAno(matricula, valorMes, ano);
        return fechamentoJpaOpt.map(FechamentoJpaMapper::toDomain);
    }

    @Override
    public Fechamento salva(Fechamento fechamento) {
        var fechamentoJpa = FechamentoJpaMapper.toEntity(fechamento);
        fechamentoJpa = fechamentoJpaRepository.save(fechamentoJpa);
        return FechamentoJpaMapper.toDomain(fechamentoJpa);
    }
}
