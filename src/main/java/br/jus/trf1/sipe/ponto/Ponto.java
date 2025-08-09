package br.jus.trf1.sipe.ponto;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.PedidoAlteracao;
import br.jus.trf1.sipe.folha.Folha;
import br.jus.trf1.sipe.registro.Registro;
import br.jus.trf1.sipe.registro.Sentido;
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
@Table(name = "pontos", schema = "sispontodb")
public class Ponto {

    @EmbeddedId
    private PontoId id;

    private Float indice;

    private String descricao;

    @Transient
    @Builder.Default
    private Integer numeroRegistrosCalculados = 0;

    @Transient
    @Builder.Default
    private Duration horasPermanencia = Duration.ZERO;

    @OneToMany(mappedBy = "ponto", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Registro> registros;

    @OneToMany(mappedBy = "ponto", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PedidoAlteracao> pedidos;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumns(value = {
            @JoinColumn(name = "id_servidor_folha", referencedColumnName = "id_servidor"),
            @JoinColumn(name = "ano_folha", referencedColumnName = "ano"),
            @JoinColumn(name = "mes_folha", referencedColumnName = "mes")
    }, foreignKey = @ForeignKey(name = "fk_folha_ponto"))
    private Folha folha;

    public IndicePonto getIndice() {
        return IndicePonto.toEnum(indice);
    }

    public void setIndice(IndicePonto indicePonto) {
        this.indice = indicePonto.getValor();
    }


    public Duration getHorasPermanencia() {
        log.info("getHorasPermanencia - buscando registros do ponto id:{}", this.id);
        var registros = getRegistros();
        if (registros == null || registros.isEmpty()) {
            log.info("getHorasPermanencia - registros encontrados: 0");
            return Duration.ZERO;
        }
        registros = getRegistros().stream().filter(r -> r.getRegistroNovo() == null && r.getAtivo()).toList();
        log.info("getHorasPermanencia - registros encontrados:{}", registros.size());
        if (horasPermanencia.isZero() || !Objects.equals(numeroRegistrosCalculados, registros.size())) {
            horasPermanencia = calculaHorasPermanencia(this);
            // Atualiza o contador de registros calculados para evitar recálculos desnecessários
            numeroRegistrosCalculados = registros.size();
            log.info("horas {}", horasPermanencia.toString());
        }
        return horasPermanencia;
    }

    public boolean pedidoAlteracaoPendente() {
        if (pedidos == null) {
            return false;
        }
        var i = pedidos.stream().filter(pedidoAlteracao -> pedidoAlteracao.getDataAprovacao() == null).count();
        log.info("Pedido alteraço de pendente: {}", i);
        return i > 0;
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
        return Duration.ofSeconds((long) (totalHoras.getSeconds() * ponto.getIndice().getValor()));
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
