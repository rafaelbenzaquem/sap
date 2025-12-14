package br.jus.trf1.sipe.folha.infrastructure.jpa;

import br.jus.trf1.sipe.folha.domain.model.Folha;
import br.jus.trf1.sipe.folha.domain.port.out.FolhaPersistencePort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FolhaJpaPersistenceAdapter implements FolhaPersistencePort {

    private final FolhaJpaRepository folhaJpaRepository;

    public FolhaJpaPersistenceAdapter(FolhaJpaRepository folhaJpaRepository) {
        this.folhaJpaRepository = folhaJpaRepository;
    }

    @Override
    public Folha salva(Folha folha) {
        FolhaJpa folhaJpa = FolhaJpaMapper.toEntity(folha);
        folhaJpa = folhaJpaRepository.save(folhaJpa);
        return FolhaJpaMapper.toDomain(folhaJpa);
    }

    @Override
    public Optional<Folha> buscaFolha(String matricula, Integer valor, int ano) {
       var folhaJpaOpt = folhaJpaRepository.buscarFolha(matricula, valor, ano);
        return folhaJpaOpt.map(FolhaJpaMapper::toDomain);
    }
}
