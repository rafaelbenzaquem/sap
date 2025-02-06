package br.jus.trf1.sap.relatorio;

import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.ponto.PontoRepository;
import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioService {

    private final PontoRepository pontoRepository;

    public RelatorioService(PontoRepository pontoRepository) {
        this.pontoRepository = pontoRepository;
    }

    public void gerarRelatorio(Integer matricula, LocalDate inicio, LocalDate fim) {

        List<Ponto> pontos = pontoRepository.buscarPontosPorMatriculaMaisRangeDeData(matricula, inicio, fim);

        Map<String,Object> parametros = new HashMap<>();

        pontos.forEach(ponto -> {
           ponto.getRegistros().forEach(registro -> {
               parametros.put("dia", ponto.getId().getDia());
               parametros.put("hora", registro.getHora());
               parametros.put("sentido", registro.getSentido());
               parametros.put("descricao", ponto.getDescricao());
           }) ;
        });
        String pathRelatorio = "D:\\rr20178.adm\\Desktop\\sisponto\\sap\\src\\main\\resources\\relatorios\\folha_de_ponto_A4.jrxml";
         String destino = "D:\\rr20178.adm\\Documents\\xRelatorios\\teste.pdf";
        try {
            JasperReport report = JasperCompileManager.compileReport(pathRelatorio);
            JasperPrint print = JasperFillManager.fillReport(report, parametros, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfFile(print, destino);

        } catch (JRException e) {
            throw new RuntimeException(e);
        }

    }


}
