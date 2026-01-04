package br.jus.trf1.sipe.ausencia.ausencia.infrastructure.jpa;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;
import br.jus.trf1.sipe.ausencia.ausencia.domain.port.out.AusenciaPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AusenciaJpaAdapter implements AusenciaPersistencePort {

    private final AusenciaJpaRepository ausenciaJpaRepository;

    public AusenciaJpaAdapter(AusenciaJpaRepository ausenciaJpaRepository) {
        this.ausenciaJpaRepository = ausenciaJpaRepository;
    }

    @Override
    public Ausencia salva(Ausencia ausencia) {
        var ausenciaJpa = AusenciaJpaMapper.toEntity(ausencia);
        ausenciaJpa = ausenciaJpaRepository.save(ausenciaJpa);
        return AusenciaJpaMapper.toDomain(ausenciaJpa);
    }

    @Override
    public List<Ausencia> listaPorPeriodo(String matricula, LocalDate dataInicio, LocalDate dataFim) {
        return ausenciaJpaRepository.listaAusenciasDoServidorNoPeriodo(matricula, dataInicio, dataFim).stream().map(AusenciaJpaMapper::toDomain).toList();
    }

    @Transactional
    @Override
    public List<Ausencia> substituiTodos(List<Ausencia> ausenciasAntigas, List<Ausencia> ausenciasNovas) {
        if (ausenciasNovas == null || ausenciasNovas.isEmpty())
            throw new IllegalArgumentException("Lista de ausencias novas não pode ser vazia!");
        if (ausenciasAntigas != null && !ausenciasAntigas.isEmpty())
            ausenciaJpaRepository.deleteAll(ausenciasAntigas.stream().map(AusenciaJpaMapper::toEntity).toList());
        var ausenciasSalvas = ausenciaJpaRepository.saveAll(ausenciasNovas.stream().map(AusenciaJpaMapper::toEntity).toList());
        return ausenciasSalvas.stream().map(AusenciaJpaMapper::toDomain).toList();
    }

    @Override
    public List<Ausencia> salvaTodos(List<Ausencia> ausenciasNovas) {
        var ausenciasParaSalve = ausenciasNovas.stream().map(AusenciaJpaMapper::toEntity).toList();
        return ausenciaJpaRepository.saveAll(ausenciasParaSalve).stream().map(AusenciaJpaMapper::toDomain).toList();
    }

    @Override
    public Optional<Ausencia> buscaNoDia(String matricula, LocalDate dia) {
        return ausenciaJpaRepository.buscaAusenciaDoServidorNoDia(matricula, dia).map(AusenciaJpaMapper::toDomain);
    }

    @Transactional
    @Override
    public Ausencia substitui(Ausencia ausenciaAntiga, Ausencia ausenciaNova) {
        var ausensiaAntigaJpa = AusenciaJpaMapper.toEntity(ausenciaAntiga);
        ausenciaJpaRepository.delete(ausensiaAntigaJpa);
        var ausenciaNovaJpa = AusenciaJpaMapper.toEntity(ausenciaNova);
        ausenciaNovaJpa = ausenciaJpaRepository.save(ausenciaNovaJpa);
        return AusenciaJpaMapper.toDomain(ausenciaNovaJpa);
    }
}
