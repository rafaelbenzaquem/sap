package br.jus.trf1.sipe.servidor;

import br.jus.trf1.sipe.ausencia.Ausencia;
import br.jus.trf1.sipe.lotacao.application.jsarh.LotacaoJSarhMapper;
import br.jus.trf1.sipe.servidor.application.jsarh.ServidorJSarh;
import br.jus.trf1.sipe.servidor.infrastructure.persistence.ServidorJpa;
import br.jus.trf1.sipe.usuario.infrastructure.db.UsuarioJpa;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ServidorCreator {

    private ServidorCreator() {
    }

    public static ServidorJpa createServidor(UsuarioJpa usuarioJPA, ServidorJSarh servidorJSarh, List<Ausencia> ausencias) {
        var servidor = new ServidorJpa(usuarioJPA, servidorJSarh.getEmail(), servidorJSarh.getFuncao(),
                servidorJSarh.getCargo(), LotacaoJSarhMapper.toEntity(servidorJSarh.getLotacao()));
        servidor.setAusencias(ausencias);
        return servidor;
    }

    public static ServidorJpa createServidor(UsuarioJpa usuarioJPA, ServidorJSarh servidorJSarh) {
        log.info("Vinculando dados do SARH para usuário: {}", usuarioJPA.getMatricula());
        return new ServidorJpa(usuarioJPA, servidorJSarh.getEmail(), servidorJSarh.getFuncao(),
                servidorJSarh.getCargo(), LotacaoJSarhMapper.toEntity(servidorJSarh.getLotacao()));
    }

}
