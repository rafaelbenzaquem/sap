package br.jus.trf1.sap.vinculo;

import br.jus.trf1.sap.vinculo.exceptions.VinculoInexistenteException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VinculoService {

    private final VinculoRepository vinculoRepository;

    public VinculoService(VinculoRepository vinculoRepository) {
        this.vinculoRepository = vinculoRepository;
    }

    public List<Vinculo> listar() {
        return vinculoRepository.findAll();
    }

    public Vinculo buscaPorMatricula(String matricula) {
        return vinculoRepository.findVinculoByMatricula(matricula).
                orElseThrow(() -> new VinculoInexistenteException("Não existe vínculo para matrícula: %s!"
                        .formatted(matricula)));
    }


}
