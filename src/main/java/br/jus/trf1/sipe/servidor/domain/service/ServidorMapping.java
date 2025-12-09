package br.jus.trf1.sipe.servidor.domain.service;

import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import br.jus.trf1.sipe.servidor.externo.jsarh.to.ServidorExternoTO;

public class ServidorMapping {
    public static Servidor toModel(Servidor servidor, ServidorExternoTO servidorExterno) {
        servidor.setCargo(servidorExterno.getCargo());
        servidor.setFuncao(servidorExterno.getFuncao());
        servidor.setEmail(servidorExterno.getEmail());
        return servidor;
    }
}