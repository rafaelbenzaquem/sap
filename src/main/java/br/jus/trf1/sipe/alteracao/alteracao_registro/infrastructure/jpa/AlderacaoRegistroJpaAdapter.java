package br.jus.trf1.sipe.alteracao.alteracao_registro.infrastructure.jpa;

import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.AlteracaoRegistro;
import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.port.out.AlteracaoRegistroPersistencePort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AlderacaoRegistroJpaAdapter implements AlteracaoRegistroPersistencePort {


    private final AlteracaoRegistroJpaRepository repository;

    public AlderacaoRegistroJpaAdapter(AlteracaoRegistroJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public AlteracaoRegistro salva(AlteracaoRegistro alteracaoRegistro) {
        var alteracaoRegistroJpa = AlteracaoRegistroJpaMapper.toEntity(alteracaoRegistro);
        alteracaoRegistroJpa = repository.save(alteracaoRegistroJpa);
        return AlteracaoRegistroJpaMapper.toDomain(alteracaoRegistroJpa);
    }

    @Override
    public Optional<AlteracaoRegistro> buscaPorId(Long id) {
        return repository.findById(id).map(AlteracaoRegistroJpaMapper::toDomain);
    }

    @Override
    public void apaga(AlteracaoRegistro alteracaoRegistro) {
        var alteracaoRegistroJpa = AlteracaoRegistroJpaMapper.toEntity(alteracaoRegistro);
        repository.delete(alteracaoRegistroJpa);
    }
}
