package br.jus.trf1.sipe.servidor;

import br.jus.trf1.sipe.externo.jsarh.servidor.dto.ServidorExternoResponse;
import br.jus.trf1.sipe.ausencia.Ausencia;
import br.jus.trf1.sipe.usuario.Usuario;

import java.util.List;

public class ServidorMapping {

    private ServidorMapping(){
    }

    public static Servidor toModel(Usuario usuario, ServidorExternoResponse servidorExternoResponse, List<Ausencia> ausencias) {
        var servidor = new Servidor(usuario, servidorExternoResponse.getEmail(), servidorExternoResponse.getFuncao(),
                servidorExternoResponse.getCargo(), servidorExternoResponse.getSiglaLotacao(), servidorExternoResponse.getDescricaoLotacao());
        servidor.setAusencias(ausencias);
        return servidor;
    }

    public static Servidor toModel(Usuario usuario, ServidorExternoResponse servidorExternoResponse) {
        return new Servidor(usuario, servidorExternoResponse.getEmail(), servidorExternoResponse.getFuncao(),
                servidorExternoResponse.getCargo(), servidorExternoResponse.getSiglaLotacao(), servidorExternoResponse.getDescricaoLotacao());
    }

}
