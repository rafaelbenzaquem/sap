package br.jus.trf1.sap.ponto;

import br.jus.trf1.sap.registro.Registro;
import br.jus.trf1.sap.registro.Sentido;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "pontos",schema = "sispontodb")
public class Ponto {

    @EmbeddedId
    private PontoId id;

    private Float indice;

    private String descricao;

    @Transient
    private Integer numeroRegistrosCalculados = 0;

    @Transient
    private Duration horasPermanencia = Duration.ZERO;

    @OneToMany(mappedBy = "ponto", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Registro> registros;


    public IndicePonto getIndice() {
        return IndicePonto.toEnum(indice);
    }

    public void setIndice(IndicePonto indicePonto) {
        this.indice = indicePonto.getValor();
    }


    public Duration getHorasPermanencia() {
        log.info("getHorasPermanencia - buscando registros do ponto id:{}", this.id);
        registros = getRegistros();

        log.info("getHorasPermanencia - registros encontrados:{}", registros.size());

        if (horasPermanencia.isZero() || (registros != null && !Objects.equals(numeroRegistrosCalculados, registros.size()))) {
            horasPermanencia = calculaHorasPermanencia(this);
            log.info("horas {}", horasPermanencia.toString());
        }
        return horasPermanencia;
    }

    /**
     * Calcula a permanência do ponto.
     *
     * @return Duração da permanência.
     */
    private Duration calculaHorasPermanencia() {
        log.info("calculaHorasPermanencia");
        Duration totalHoras = Duration.ZERO;
        if (registros == null || registros.isEmpty() || indice == null || indice == 0) {
            return totalHoras;
        }
        LocalTime entradaPendente = null;
        var registrosClassificados = new ArrayList<>(registros);
        Collections.sort(registrosClassificados);
        for (Registro registro : registrosClassificados) {
            if (registro.getSentido() == Sentido.ENTRADA) {
                entradaPendente = registro.getHora();
            } else if (registro.getSentido() == Sentido.SAIDA && entradaPendente != null) {
                totalHoras = totalHoras.plus(Duration.between(entradaPendente, registro.getHora()));
                entradaPendente = null;
            }
        }
        return Duration.ofSeconds((long) (totalHoras.getSeconds() * indice));
    }

    /**
     * Calcula a permanência em um ponto específico.
     *
     * @param ponto Ponto a ser calculado.
     * @return Duração da permanência.
     */
    public static Duration calculaHorasPermanencia(Ponto ponto) {
        log.debug("Calculando horas permanencia...");
        Duration totalHoras = Duration.ZERO;
        LocalTime entradaPendente = null;
        var registrosClassificados = new ArrayList<>(ponto.getRegistros());
        Collections.sort(registrosClassificados);
        for (Registro registro : registrosClassificados) {
            if (registro.getSentido() == Sentido.ENTRADA) {
                entradaPendente = registro.getHora();
            } else if (registro.getSentido() == Sentido.SAIDA && entradaPendente != null) {
                totalHoras = totalHoras.plus(Duration.between(entradaPendente, registro.getHora()));
                entradaPendente = null;
            }
        }
        return totalHoras;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Ponto ponto)) return false;

        return Objects.equals(id, ponto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /**
     * @return String de Ponto no formato Json
     */
    @Override
    public String toString() {
        return """
                {
                    "id":%s,
                    "descricao": "%s"
                }""".formatted(id, descricao);
    }
}
