package br.jus.trf1.sipe.comum.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class CamposUnicosExistentesException extends RuntimeException {

    private final Map<String, String> mapCampoUnicoMensagem;

    public CamposUnicosExistentesException(Map<String, String> mapCampoUnicoMensagem) {
        this(formataMensagem(mapCampoUnicoMensagem), mapCampoUnicoMensagem);
    }


    public CamposUnicosExistentesException(String message, Map<String, String> mapCampoUnicoMensagem) {
        super(message);
        this.mapCampoUnicoMensagem = mapCampoUnicoMensagem;
    }


    private static String formataMensagem(Map<String, String> mapCamposMensagens) {
        StringBuilder mensagem = new StringBuilder(mapCamposMensagens.size() == 1 ? "Campo {" : "Campos {");
        var count = 1;
        for (Map.Entry<String, String> campo : mapCamposMensagens.entrySet()) {

            mensagem.append(campo.getKey()).append(" : ").append(campo.getValue());
            if (mapCamposMensagens.size() != count) {
                mensagem.append(", ");
            }
            count++;
        }
        mensagem.append("}");

        return mensagem.toString();
    }
}
