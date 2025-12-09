package br.jus.trf1.sipe.servidor;

import br.jus.trf1.sipe.ausencia.Ausencia;
import br.jus.trf1.sipe.servidor.externo.jsarh.ServidorExterno;
import br.jus.trf1.sipe.lotacao.LotacaoMapping;
import br.jus.trf1.sipe.usuario.infrastructure.persistence.UsuarioJpa;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ServidorMapping {

    private ServidorMapping() {
    }

    public static Servidor toModel(UsuarioJpa usuarioJPA, ServidorExterno servidorExterno, List<Ausencia> ausencias) {
        var servidor = new Servidor(usuarioJPA, servidorExterno.getEmail(), servidorExterno.getFuncao(),
                servidorExterno.getCargo(), LotacaoMapping.toModel(servidorExterno.getLotacao()));
        servidor.setAusencias(ausencias);
        return servidor;
    }

    public static Servidor toModel(UsuarioJpa usuarioJPA, ServidorExterno servidorExterno) {
        log.info("Vinculando dados do SARH para usuário: {}", usuarioJPA.getMatricula());
        return new Servidor(usuarioJPA, servidorExterno.getEmail(), servidorExterno.getFuncao(),
                servidorExterno.getCargo(), LotacaoMapping.toModel(servidorExterno.getLotacao()));
    }

}
