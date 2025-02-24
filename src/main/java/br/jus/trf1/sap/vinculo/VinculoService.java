package br.jus.trf1.sap.vinculo;

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

    public Vinculo buscaPorMatricula(Integer matricula) {
        return vinculoRepository.findVinculoByMatricula(matricula).
                orElseThrow(() -> new VinculoInexistenteException("Vinculo matrícula = %d não encontrado!".formatted(matricula)));
    }


}
