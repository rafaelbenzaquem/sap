package br.jus.trf1.sap.vinculo;

import br.jus.trf1.sap.vinculo.exceptions.VinculoInexistenteException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VinculoService {

    private final VinculoRepository vinculoRepository;

    public VinculoService(VinculoRepository vinculoRepository) {
        this.vinculoRepository = vinculoRepository;
    }


    public Page<Vinculo> buscarVinculosPorNomeOuCrachaOuMatricula(String nome,
                                                                  String cracha,
                                                                  String matricula,
                                                                  Pageable pageable) {
        return vinculoRepository.findAllByNomeOrCrachaOrMatricula(nome, cracha, matricula, pageable);
    }

    public Page<Vinculo> listar(Pageable pageable) {
        return vinculoRepository.findAll(pageable);
    }

    public List<Vinculo> listar() {
        return vinculoRepository.findAll();
    }

    public Vinculo buscaPorMatricula(String matricula) {
        return vinculoRepository.findVinculoByMatricula(matricula).
                orElseThrow(() -> new VinculoInexistenteException("Não existe vínculo para matrícula: %s!"
                        .formatted(matricula)));
    }

    public Vinculo buscaPorId(Integer id) {
        return vinculoRepository.findById(id).
                orElseThrow(() -> new VinculoInexistenteException("Não existe vínculo id: %s!"
                        .formatted(id)));
    }




}
