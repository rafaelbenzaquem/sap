package br.jus.trf1.sap.relatorio;

public class RelatorioModel{
    private Integer numero;
    private String dia;
    private String hora;
    private String sentido;
    private String descricao;

    public RelatorioModel() {
    }

    public RelatorioModel(Integer numero, String dia, String hora, String sentido, String descricao) {
        this.numero = numero;
        this.dia = dia;
        this.hora = hora;
        this.sentido = sentido;
        this.descricao = descricao;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getSentido() {
        return sentido;
    }

    public void setSentido(String sentido) {
        this.sentido = sentido;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
