package br.jus.trf1.sap.relatorio.model;

import lombok.Getter;

import java.time.LocalTime;

import static br.jus.trf1.sap.util.ConstantesDataTempoUtil.PADRAO_SAIDA_TEMPO;
import static br.jus.trf1.sap.util.DataTempoUtil.tempoParaString;

/**
 * Representa um registro de ponto no relatório.
 */
@Getter
public class RegistroModel {

    public static final RegistroModel VAZIO = new RegistroModel(" ", null);

    private final String sentido;
    private final LocalTime hora;

    private RegistroModel(String sentido, LocalTime hora) {
        this.sentido = sentido;
        this.hora = hora;
    }

    /**
     * Retorna a hora formatada como texto.
     *
     * @return Hora formatada ou " " se a hora for nula.
     */
    public String getTextoHora() {
        if (hora == null) {
            return " ";
        }
        return tempoParaString(hora, PADRAO_SAIDA_TEMPO);
    }

    /**
     * Cria uma instância de RegistroModel.
     *
     * @param sentido Sentido do registro (Entrada/Saída).
     * @param hora    Hora do registro.
     * @return Instância de RegistroModel.
     */
    public static RegistroModel of(String sentido, LocalTime hora) {
        return new RegistroModel(sentido, hora);
    }

}
