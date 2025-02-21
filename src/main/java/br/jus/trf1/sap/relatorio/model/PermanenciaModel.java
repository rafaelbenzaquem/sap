package br.jus.trf1.sap.relatorio.model;

import lombok.*;

import java.time.Duration;

@Getter
public class PermanenciaModel {

    public static final PermanenciaModel ZERO = PermanenciaModel.of(Duration.ZERO);

    private static final String PADRAO_TEXTO_PERMANENCIA_TOTAL = "%02d:%02d:%02d";
    private final Duration permanencia;
    private final String textoPermanencia;

    private PermanenciaModel(Duration permanencia, String textoPermanencia) {
        this.permanencia = permanencia;
        this.textoPermanencia = textoPermanencia;
    }


    public static PermanenciaModel of(Duration permanencia) {
        return new PermanenciaModel(permanencia, formataTextoPermanencia(permanencia));
    }

    private static String formataTextoPermanencia(Duration permanencia) {
        var horas = permanencia.toHours();
        var minutos = permanencia.toMinutes();
        var segundos = permanencia.toSeconds();

        return PADRAO_TEXTO_PERMANENCIA_TOTAL.formatted(horas,
                horas == 0 ? minutos : minutos % (horas * 60),
                minutos == 0 ? segundos : segundos % (minutos * 60));
    }
}
