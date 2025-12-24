package br.jus.trf1.sipe.registro.infrastructure.jpa;

import br.jus.trf1.sipe.registro.domain.model.Registro;
import br.jus.trf1.sipe.registro.domain.port.out.RegistroPersistencePort;
import br.jus.trf1.sipe.registro.exceptions.RegistroInexistenteException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RegistroJpaAdapter implements RegistroPersistencePort {

    private final RegistroJpaRepository registroJpaRepository;

    public RegistroJpaAdapter(RegistroJpaRepository registroJpaRepository) {
        this.registroJpaRepository = registroJpaRepository;
    }

    @Override
    public Registro buscaPorId(Long id) {
        return registroJpaRepository.findById(id).map(RegistroJpaMapper::toDomain).orElseThrow(() -> new RegistroInexistenteException(id));
    }

    @Override
    public Boolean existe(Long id) {
        return registroJpaRepository.existsById(id);
    }

    @Override
    public List<Registro> listaAtuaisDoPonto(String matricula, LocalDate dia, boolean apenasAtivos) {

        if (apenasAtivos) {
            return registroJpaRepository.listaRegistrosAtuaisAtivosDoPonto(matricula, dia).stream().map(RegistroJpaMapper::toDomain).toList();
        }
        return registroJpaRepository.listaAtuaisDoPonto(matricula, dia).stream().map(RegistroJpaMapper::toDomain).toList();
    }

    @Override
    public List<Registro> salvaTodos(List<Registro> registros) {
        var registrosJpa = registros.stream().map(RegistroJpaMapper::toEntity).toList();
        registrosJpa = registroJpaRepository.saveAll(registrosJpa);
        return registrosJpa.stream().map(RegistroJpaMapper::toDomain).toList();
    }

    @Override
    public List<Registro> listaRegistrosProvenientesDoSistemaExternoPorPonto(String matricula, LocalDate dia) {
        return registroJpaRepository.listaProvenientesDoSistemaExterno(matricula, dia).stream().map(RegistroJpaMapper::toDomain).toList();
    }

    @Override
    public Registro salvar(Registro registro) {
        var registroJpa = RegistroJpaMapper.toEntity(registro);
        registroJpa = registroJpaRepository.save(registroJpa);
        return RegistroJpaMapper.toDomain(registroJpa);
    }

    @Override
    public void apagarPorId(Long idRegistro) {
        buscaPorId(idRegistro);
        registroJpaRepository.deleteById(idRegistro);
    }
}
