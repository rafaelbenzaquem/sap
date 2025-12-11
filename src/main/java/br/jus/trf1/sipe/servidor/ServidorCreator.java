package br.jus.trf1.sipe.servidor;

import br.jus.trf1.sipe.ausencia.Ausencia;
import br.jus.trf1.sipe.servidor.application.jsarh.ServidorJSarh;
import br.jus.trf1.sipe.lotacao.LotacaoMapping;
import br.jus.trf1.sipe.servidor.infrastructure.persistence.ServidorJpa;
import br.jus.trf1.sipe.usuario.infrastructure.persistence.UsuarioJpa;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ServidorCreator {

    private ServidorCreator() {
    }

    public static ServidorJpa createServidor(UsuarioJpa usuarioJPA, ServidorJSarh servidorExterno, List<Ausencia> ausencias) {
        var servidor = new ServidorJpa(usuarioJPA, servidorExterno.getEmail(), servidorExterno.getFuncao(),
                servidorExterno.getCargo(), LotacaoMapping.toEntity(servidorExterno.getLotacao()));
        servidor.setAusencias(ausencias);
        return servidor;
    }

    public static ServidorJpa createServidor(UsuarioJpa usuarioJPA, ServidorJSarh servidorExterno) {
        log.info("Vinculando dados do SARH para usuário: {}", usuarioJPA.getMatricula());
        return new ServidorJpa(usuarioJPA, servidorExterno.getEmail(), servidorExterno.getFuncao(),
                servidorExterno.getCargo(), LotacaoMapping.toEntity(servidorExterno.getLotacao()));
    }

}
