package br.jus.trf1.sipe.ponto.infrastructure.jpa;

import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.ponto.domain.port.out.PontoPersistencePort;
import br.jus.trf1.sipe.ponto.exceptions.PontoExistenteException;
import br.jus.trf1.sipe.ponto.exceptions.PontoInexistenteException;
import br.jus.trf1.sipe.registro.domain.model.Registro;
import br.jus.trf1.sipe.registro.infrastructure.jpa.RegistroJpa;
import br.jus.trf1.sipe.registro.infrastructure.jpa.RegistroJpaMapper;
import br.jus.trf1.sipe.registro.infrastructure.jpa.RegistroJpaRepository;
import br.jus.trf1.sipe.usuario.infrastructure.jpa.UsuarioJpa;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PontoJpaPersistenceAdapter implements PontoPersistencePort {


    private final PontoJpaRepository pontoJpaRepository;
    private final RegistroJpaRepository registroJpaRepository;

    public PontoJpaPersistenceAdapter(PontoJpaRepository pontoJpaRepository, RegistroJpaRepository registroJpaRepository) {
        this.pontoJpaRepository = pontoJpaRepository;
        this.registroJpaRepository = registroJpaRepository;
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
    public List<Ponto> listaPorPeriodo(String matricula, LocalDate inicio, LocalDate fim) {
        return pontoJpaRepository.buscaPontosPorPeriodo(matricula, inicio, fim)
                .stream().map(PontoJpaMapper::toDomain).toList();
    }

    @Transactional
    @Override
    public Ponto salva(Ponto ponto, List<Registro> registros) {
        var pontoJpa = PontoJpaMapper.toEntity(ponto);
        var id = pontoJpa.getId();
        if (pontoJpaRepository.existsById(id)) {
            throw new PontoExistenteException(id.getUsuario().getMatricula(), id.getDia());
        }
        return salvaPontoComRegistros(pontoJpa, registros);
    }

    @Transactional
    @Override
    public Ponto atualiza(Ponto ponto, List<Registro> registros) {
        var pontoJpa = PontoJpaMapper.toEntity(ponto);
        var id = pontoJpa.getId();
        if (pontoJpaRepository.existsById(id)) {
            throw new PontoInexistenteException(id.getUsuario().getMatricula(), id.getDia());
        }
        return salvaPontoComRegistros(pontoJpa, registros);
    }

    private Ponto salvaPontoComRegistros(PontoJpa pontoJpa, List<Registro> registros) {
        pontoJpa = pontoJpaRepository.save(pontoJpa);
        List<RegistroJpa> registrosJpa = new ArrayList<>();
        for (var registro : registros) {
            var registroJpa = RegistroJpaMapper.toEntity(registro);
            registroJpa.setPonto(pontoJpa);
            registrosJpa.add(registroJpa);
        }
        registrosJpa = registroJpaRepository.saveAll(registrosJpa);
        pontoJpa.setRegistros(registrosJpa);
        pontoJpaRepository.save(pontoJpa);
        return PontoJpaMapper.toDomain(pontoJpa);
    }
}
