package br.jus.trf1.sipe.ausencia.ausencia.infrastructure.jpa;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;
import br.jus.trf1.sipe.ausencia.ausencia.domain.port.out.AusenciaPersistencePort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AusenciaJpaAdapter implements AusenciaPersistencePort {

    private final AusenciaJpaRepository ausenciaJpaRepository;

    public AusenciaJpaAdapter(AusenciaJpaRepository ausenciaJpaRepository) {
        this.ausenciaJpaRepository = ausenciaJpaRepository;
    }

    @Override
    public List<Ausencia> listaAusenciasPorServidorMaisPeriodo(String matricula, LocalDate dataInicio, LocalDate dataFim) {
        return ausenciaJpaRepository.listaAusenciasPorServidorMaisPeriodo(matricula, dataInicio, dataFim).stream().map(AusenciaJpaMapper::toDomain).toList();
    }

    @Override
    public void deletaTodos(List<Ausencia> ausenciasParaDelete) {
        ausenciaJpaRepository.deleteAll(ausenciasParaDelete.stream().map(AusenciaJpaMapper::toEntity).toList());
    }

    @Override
    public Ausencia salva(Ausencia ausencia) {
        var ausenciaJpa = AusenciaJpaMapper.toEntity(ausencia);
        ausenciaJpa = ausenciaJpaRepository.save(ausenciaJpa);
        return AusenciaJpaMapper.toDomain(ausenciaJpa);
    }
}
