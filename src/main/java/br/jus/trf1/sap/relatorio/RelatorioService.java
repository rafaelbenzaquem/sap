package br.jus.trf1.sap.relatorio;

import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.ponto.PontoRepository;
import br.jus.trf1.sap.util.DateTimeUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.jus.trf1.sap.util.DateTimeUtils.*;

@Service
public class RelatorioService {

    private final PontoRepository pontoRepository;

    public RelatorioService(PontoRepository pontoRepository) {
        this.pontoRepository = pontoRepository;
    }

    public void gerarRelatorio(Integer matricula, LocalDate inicio, LocalDate fim) {

        List<Ponto> pontos = pontoRepository.buscarPontosPorMatriculaMaisRangeDeData(matricula, inicio, fim);

        Map<String, Object> parametros = new HashMap<>();

        List<RelatorioModel> pontoDataSet = new ArrayList<>();


        pontos.forEach(p -> p.getRegistros().forEach(r ->
                pontoDataSet.add(new RelatorioModel(0, dataParaString(p.getId().getDia()),
                tempoParaString(r.getHora()),r.getSentido().equals('S') ? "Saída" : "Entrada",
                p.getDescricao()))));

        JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(pontoDataSet);


        parametros.put("PontoDataset", datasource);

        String pathRelatorioComp = "C:\\Users\\rafae\\IdeaProjects\\sap\\src\\main\\resources\\relatorios\\Ponto_A4.jasper";
        String pathRelatorio = "C:\\Users\\rafae\\IdeaProjects\\sap\\src\\main\\resources\\relatorios\\Ponto_A4.jrxml";
        String destino = "C:\\Users\\rafae\\relatorio\\teste3.pdf";
        try {
            File imgPath = new File(pathRelatorioComp);

            InputStream stream = new FileInputStream(imgPath);

//            JasperReport report = JasperCompileManager.compileReport(pathRelatorio);
            JasperPrint print = JasperFillManager.fillReport(stream, parametros, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfFile(print, destino);

        } catch (JRException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    public byte[] extractBytes(String pathImage) throws IOException {
        // open image
        File imgPath = new File(pathImage);
        BufferedImage bufferedImage = ImageIO.read(imgPath);

        // get DataBufferBytes from Raster
        WritableRaster raster = bufferedImage.getRaster();
        DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

        return (data.getData());
    }

}
