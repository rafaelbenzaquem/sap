package br.jus.trf1.sap.relatorio.model;

import lombok.Getter;

import java.time.LocalTime;

import static br.jus.trf1.sap.comum.util.ConstantesParaDataTempo.PADRAO_SAIDA_TEMPO;
import static br.jus.trf1.sap.comum.util.DataTempoUtil.paraString;

/**
 * Representa um registro de ponto no relatório.
 */
@Getter
public class RegistroModel implements Comparable<RegistroModel> {

    public static final RegistroModel VAZIO = new RegistroModel("-----", null);

    private final String sentido;
    private final LocalTime hora;
    private RegistroModel oRegistroModel;

    private RegistroModel(String sentido, LocalTime hora) {
        this.sentido = sentido;
        this.hora = hora;
    }

    /**
     * Retorna a hora formatada como texto.
     *
     * @return Hora formatada ou "--:--" se a hora for nula.
     */
    public String getTextoHora() {
        if (hora == null) {
            return "--:--";
        }
        return paraString(hora, PADRAO_SAIDA_TEMPO);
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

    @Override
    public int compareTo(RegistroModel oRegistroModel) {
        if (hora == null) {
            return 1;
        }
        return hora.compareTo(oRegistroModel.getHora());
    }
}
