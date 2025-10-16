package br.jus.trf1.sipe.relatorio;

import net.sf.jasperreports.engine.JRException;

import java.time.LocalDate;

public interface RelatorioService {

    byte[] gerarRelatorio(String matricula, LocalDate inicio, LocalDate fim) throws JRException;
}
