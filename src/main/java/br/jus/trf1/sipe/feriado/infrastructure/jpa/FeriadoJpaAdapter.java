package br.jus.trf1.sipe.feriado.infrastructure.jpa;

import br.jus.trf1.sipe.feriado.domain.model.Feriado;
import br.jus.trf1.sipe.feriado.domain.port.out.FeriadoPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FeriadoJpaAdapter implements FeriadoPersistencePort {

    private final FeriadoJpaRepository repository;

    public FeriadoJpaAdapter(FeriadoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Feriado> busca(LocalDate data) {
        return repository.findById(data).map(FeriadoJpaMapper::toDomain);
    }

    @Override
    public List<Feriado> listaPorPeriodo(LocalDate inicio, LocalDate fim) {
        return repository.lista(inicio, fim).stream().map(FeriadoJpaMapper::toDomain).toList();
    }

    @Override
    public void apaga(LocalDate data) {
        repository.deleteById(data);
    }

    @Override
    public List<Feriado> listaPorAno(int ano) {
        var inicio = LocalDate.of(ano, 1, 1);
        var fim = LocalDate.of(ano, 12, 31);
        return listaPorPeriodo(inicio, fim);
    }

    @Transactional
    @Override
    public List<Feriado> substitui(List<Feriado> feriadosAtuais, List<Feriado> feriadosNovos) {
        if(feriadosNovos.isEmpty()&&feriadosAtuais.isEmpty()){
         return List.of();
        }else if (feriadosNovos.isEmpty()) {
            repository.deleteAll(feriadosAtuais.stream().map(FeriadoJpaMapper::toEntity).toList());
            return List.of();
        } else if (feriadosAtuais.isEmpty()) {
            return repository.saveAll(feriadosNovos.stream().map(FeriadoJpaMapper::toEntity).toList()).
                    stream().map(FeriadoJpaMapper::toDomain).toList();
        } else {
            repository.deleteAll(feriadosAtuais.stream().map(FeriadoJpaMapper::toEntity).toList());
            return repository.saveAll(feriadosNovos.stream().map(FeriadoJpaMapper::toEntity).toList()).
                    stream().map(FeriadoJpaMapper::toDomain).toList();
        }
    }
}
