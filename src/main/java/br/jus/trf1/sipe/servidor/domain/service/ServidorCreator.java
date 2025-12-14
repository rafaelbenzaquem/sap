package br.jus.trf1.sipe.servidor.domain.service;

import br.jus.trf1.sipe.ausencia.Ausencia;
import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ServidorCreator {

    private ServidorCreator() {
    }

    public static Servidor createServidor(Usuario usuarioAtual, Servidor servidorAtualizado, List<Ausencia> ausencias) {
        var servidorCriado = new Servidor(usuarioAtual, servidorAtualizado.getEmail(), servidorAtualizado.getFuncao(),
                servidorAtualizado.getCargo(), servidorAtualizado.getLotacao());
        servidorCriado.setAusencias(ausencias);
        return servidorCriado;
    }

    public static Servidor createServidor(Usuario usuarioAtual, Servidor servidorAtualizado) {
        return new Servidor(usuarioAtual, servidorAtualizado.getEmail(), servidorAtualizado.getFuncao(),
                servidorAtualizado.getCargo(), servidorAtualizado.getLotacao());
    }

}
