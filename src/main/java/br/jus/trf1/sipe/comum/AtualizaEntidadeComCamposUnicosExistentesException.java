package br.jus.trf1.sipe.comum;

import lombok.Getter;

import java.util.Map;

@Getter
public class AtualizaEntidadeComCamposUnicosExistentesException extends RuntimeException {

    private final Map<String, String> mapCamposMensagens;

    public AtualizaEntidadeComCamposUnicosExistentesException(Map<String, String> mapCamposMensagens) {
        this(formataMensagem(mapCamposMensagens), mapCamposMensagens);
    }


    public AtualizaEntidadeComCamposUnicosExistentesException(String message, Map<String, String> mapCamposMensagens) {
        super(message);
        this.mapCamposMensagens = mapCamposMensagens;
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
