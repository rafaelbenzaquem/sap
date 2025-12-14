package br.jus.trf1.sipe.ponto.infrastructure.jpa;

import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.ponto.domain.port.out.PontoPersistencePort;
import br.jus.trf1.sipe.ponto.exceptions.PontoExistenteException;
import br.jus.trf1.sipe.ponto.exceptions.PontoInexistenteException;
import br.jus.trf1.sipe.registro.RegistroService;
import br.jus.trf1.sipe.usuario.infrastructure.jpa.UsuarioJpa;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PontoJpaPersistenceAdapter implements PontoPersistencePort {


    private final PontoJpaRepository pontoJpaRepository;
    private final RegistroService registroService;

    public PontoJpaPersistenceAdapter(PontoJpaRepository pontoJpaRepository,RegistroService registroService) {
        this.pontoJpaRepository = pontoJpaRepository;
        this.registroService = registroService;
    }

    @Override
    public Boolean existePontosComAlteracaoRegistroPendentePorData(String matricula, LocalDate inicio, LocalDate fim) {
        return pontoJpaRepository.existePontosComAlteracaoRegistroPendentePorData(matricula, inicio, fim);
    }

    @Override
    public boolean existe(String matricula, LocalDate dia) {
        return pontoJpaRepository.existsById(PontoJpaId.builder()
                        .usuario(UsuarioJpa.builder()
                                .matricula(matricula)
                                .build())
                        .dia(dia)
                .build());
    }

    @Override
    public Ponto busca(String matricula, LocalDate dia) {
        return pontoJpaRepository.buscaPonto(matricula, dia).map(PontoJpaMapper::toDomain)
                .orElseThrow(() -> new PontoInexistenteException(matricula, dia));
    }

    @Override
    public List<Ponto> buscaPontosPorPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        return pontoJpaRepository.buscaPontosPorPeriodo(matricula, inicio, fim)
                .stream().map(PontoJpaMapper::toDomain).toList();
    }

    @Transactional
    @Override
    public Ponto salva(Ponto ponto) {
        var pontoJpa = PontoJpaMapper.toEntity(ponto);
        var id = pontoJpa.getId();
        if (pontoJpaRepository.existsById(id)) {
            throw new PontoExistenteException(id.getUsuario().getMatricula(), id.getDia());
        }
        var pontoSalvo = PontoJpaMapper.toDomain(pontoJpaRepository.save(pontoJpa));
        var registros = registroService.atualizaRegistrosSistemaDeAcesso(pontoSalvo);
        pontoSalvo.setRegistros(registros);
        return pontoSalvo;
    }
}
