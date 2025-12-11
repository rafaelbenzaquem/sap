package br.jus.trf1.sipe.servidor.application.jsarh;

import br.jus.trf1.sipe.servidor.domain.model.Servidor;

public class ServidorJSarhMapper {
    public static Servidor toDomain(ServidorJSarh servidorJSarh) {
        return Servidor.builder()
                .matricula(servidorJSarh.getMatricula())
                .nome(servidorJSarh.getNome())
                .email(servidorJSarh.getEmail())
                .funcao(servidorJSarh.getFuncao())
                .cargo(servidorJSarh.getCargo())
                .build();
    }
}
