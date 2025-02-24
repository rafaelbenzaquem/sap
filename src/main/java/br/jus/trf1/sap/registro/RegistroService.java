package br.jus.trf1.sap.registro;

import br.jus.trf1.sap.registro.exceptions.RegistroExistenteSalvoEmPontoDifenteException;
import br.jus.trf1.sap.registro.exceptions.RegistroInexistenteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static br.jus.trf1.sap.util.DataTempoUtil.tempoParaString;

@Slf4j
@Service
public class RegistroService {

    private  final RegistroRepository registroRepository;

    public RegistroService(RegistroRepository registroRepository) {
        this.registroRepository = registroRepository;
    }

    public Registro criarNovoRegistro(Registro registro) {
        log.debug("Criando Registro - {}", tempoParaString(registro.getHora()));
        if (registro.getCodigoAcesso() == null) {
            registro.setVersao(1);
            Registro registroSalvo = registroRepository.save(registro);
            log.debug("Registro id = {}  criado com sucesso", registroSalvo.getId());
            return registroSalvo;
        }
        var registroOptional = registroRepository.findByCodigoAcesso(registro.getCodigoAcesso());
        if (registroOptional.isPresent()) {
            var registroAntigo = registroOptional.get();
            if (registroAntigo.getPonto().equals(registro.getPonto())) {
                throw new RegistroExistenteSalvoEmPontoDifenteException(
                        "Registro foi salvo anteriormente no Ponto %s - %s".
                                formatted(registroAntigo.getPonto().getId().getDia(),
                                        registroAntigo.getPonto().getId().getMatricula()));
            }
            registro.setId(null);
            registro.setCodigoAcesso(null);
            registro.setVersao(registroAntigo.getVersao() + 1);
            var registroAtualizado = registroRepository.save(registro);
            registroAntigo.setRegistroAtualizado(registroAtualizado);
            registroRepository.save(registroAntigo);
            return registroAtualizado;
        }
        registro.setVersao(1);
        return registroRepository.save(registro);
    }

    public Registro atualizaRegistro(Registro registro) {
        log.debug("Atualizando Registro - {} - {} - {}", registro.getId(), tempoParaString(registro.getHora())
                , registro.getCodigoAcesso());

        if (registro.getId() != null) {
            var registroAnterior = registroRepository.findById(registro.getId()).orElseThrow(() -> new
                    RegistroInexistenteException("Registro id - %s não existe!".formatted(registro.getId())));
            registro.setId(null);
            registro.setCodigoAcesso(null);
            registro.setVersao(registroAnterior.getVersao() + 1);
            var registroAtualizado = registroRepository.save(registro);
            registroAnterior.setRegistroAtualizado(registroAtualizado);
            return registroRepository.save(registroAnterior);

        }
        throw new IllegalArgumentException("Registro com id nulo");
    }
}
