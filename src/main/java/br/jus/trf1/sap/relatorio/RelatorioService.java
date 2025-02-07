package br.jus.trf1.sap.relatorio;

import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.ponto.PontoRepository;
import br.jus.trf1.sap.vinculo.Vinculo;
import br.jus.trf1.sap.vinculo.VinculoRepository;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

import static br.jus.trf1.sap.util.DateTimeUtils.dataParaString;
import static br.jus.trf1.sap.util.DateTimeUtils.tempoParaString;

@Service
public class RelatorioService {

    private final VinculoRepository vinculoRepository;
    private final PontoRepository pontoRepository;
    public static final String PATH = "classpath:relatorios/";

    public RelatorioService(VinculoRepository vinculoRepository, PontoRepository pontoRepository) {
        this.vinculoRepository = vinculoRepository;
        this.pontoRepository = pontoRepository;
    }

    public byte[] gerarRelatorio(Integer matricula, LocalDate inicio, LocalDate fim) throws IOException {

        var logoImagem = loadFile(PATH + "img/logoSJRRHorizontal.png");

        var pontos = pontoRepository.buscarPontosPorMatriculaMaisRangeDeData(matricula, inicio, fim);
        Optional<Vinculo> vinculoByMatricula = vinculoRepository.findVinculoByMatricula(matricula);
        var vinculo = vinculoByMatricula.get();
        Map<String, Object> parametros = new HashMap<>();

        List<RelatorioModel> pontoDataSet = new ArrayList<>();


        IntStream.range(0, pontos.size()).forEach(index -> {
            Ponto p = pontos.get(index);
            p.getRegistros().forEach(r ->
                    pontoDataSet.add(new RelatorioModel(index + 1, dataParaString(p.getId().getDia(), "dd/MM/yyyy"),
                            tempoParaString(r.getHora(), "HH:mm:ss"), r.getSentido().equals('S') ? "Saída" : "Entrada",
                            p.getDescricao())));
        });


        var datasource = new JRBeanCollectionDataSource(pontoDataSet);


        parametros.put("pontoDataset", datasource);
        parametros.put("logoImagem", logoImagem);
        parametros.put("nome", vinculo.getNome());
        parametros.put("matricula", "RR" + vinculo.getMatricula());
        parametros.put("lotacao", "SEÇÃO DE MODERNIZAÇÃO ADMINISTRATIVA/SEMAD");
        parametros.put("periodo", dataParaString(inicio, "dd/MM/yyyy")+" a "+dataParaString(fim, "dd/MM/yyyy"));


        try {
            var relatorioPonto = loadFile(PATH + "/Ponto_A4.jasper");
            var stream = new ByteArrayInputStream(relatorioPonto);
            var print = JasperFillManager.fillReport(stream, parametros, new JREmptyDataSource());
            return JasperExportManager.exportReportToPdf(print);
        } catch (JRException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] loadFile(String pathFile) throws IOException {
        var absolutePathFile = ResourceUtils.getFile(pathFile).getAbsolutePath();
        var file = new File(absolutePathFile);
        try (InputStream inputStream = new FileInputStream(file)) {
            return IOUtils.toByteArray(inputStream);
        }
    }

    public byte[] extractBytes(String pathFile) throws IOException {
        // open image
        var imgPath = new File(pathFile);
        var bufferedImage = ImageIO.read(imgPath);

        // get DataBufferBytes from Raster
        var raster = bufferedImage.getRaster();
        var data = (DataBufferByte) raster.getDataBuffer();

        return (data.getData());
    }

}
