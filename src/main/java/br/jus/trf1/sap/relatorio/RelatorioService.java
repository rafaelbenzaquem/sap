package br.jus.trf1.sap.relatorio;

import br.jus.trf1.sap.arquivo.ArquivoRepository;
import br.jus.trf1.sap.ponto.PontoRepository;
import br.jus.trf1.sap.relatorio.model.RelatorioModel;
import br.jus.trf1.sap.relatorio.model.UsuarioModel;
import br.jus.trf1.sap.vinculo.VinculoRepository;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.HashMap;

import static br.jus.trf1.sap.util.DateTimeUtils.dataParaString;
import static net.sf.jasperreports.engine.JasperExportManager.exportReportToPdf;
import static net.sf.jasperreports.engine.JasperFillManager.fillReport;

@Slf4j
@Service
public class RelatorioService {


    private static final String PADRAO_DATA = "dd/MM/yyyy";
    private final VinculoRepository vinculoRepository;
    private final PontoRepository pontoRepository;
    private final ArquivoRepository arquivoRepository;

    public RelatorioService(VinculoRepository vinculoRepository, PontoRepository pontoRepository, ArquivoRepository arquivoRepository) {
        this.vinculoRepository = vinculoRepository;
        this.pontoRepository = pontoRepository;
        this.arquivoRepository = arquivoRepository;
    }

    public byte[] gerarRelatorio(Integer matricula, LocalDate inicio, LocalDate fim) throws JRException {

        log.info("Gerando Relatorio");
        log.info("Carregando image da logo superior esquerda: {}", "logoImagem.png");
        var logoImagem = arquivoRepository.findByNome("logoImagem.png");
        log.info("Carregando image da logo superior direita: {}", "logoImagem2.png");
        var logoImagem2 = arquivoRepository.findByNome("logoImagem2.png");
        log.info("Carregando arquivo de relatório Jasper: {}", "relatorioA4.jasper");
        var relatorioPonto = arquivoRepository.findByNome("relatorioA4.jasper");

        var pontos = pontoRepository.buscarPontosPorMatriculaMaisRangeDeData(matricula, inicio, fim);
        var vinculoByMatricula = vinculoRepository.findVinculoByMatricula(matricula);
        var vinculo = vinculoByMatricula.orElseThrow();

        var usuario = UsuarioModel.builder()
                .nome(vinculo.getNome())
                .cargo("TÉCNICO JUDICIÁRIO/ APOIO ESPECIALIZADO (TECNOLOGIA DA INFORMAÇÃO)")
                .funcao("ASSISTENTE ADJUNTO III")
                .lotacao("SERVIÇO DE SUPORTE TÉCNICO AOS USUÁRIOS/SERSUT/SEINF/NUCAD/SECAD/SJRR")
                .matricula(matricula)
                .build();
        var relatorioModel = new RelatorioModel(usuario, pontos);

        var parametrosRelatorio = new HashMap<String, Object>();

        parametrosRelatorio.put("logoImagem", logoImagem.orElseThrow().getConteudo());
        parametrosRelatorio.put("logoImagem2", logoImagem2.orElseThrow().getConteudo());
        parametrosRelatorio.put("obs", "Sem observações...");
        parametrosRelatorio.put("totalHorasUteis", relatorioModel.getTextoHorasUteis());
        parametrosRelatorio.put("totalHorasPermanencia", relatorioModel.getTextoPermanenciaTotal());
        parametrosRelatorio.put("creditoDebitoLabel", relatorioModel.getRotuloHorasDebitoOuCredito());
        parametrosRelatorio.put("horaCDTotal", relatorioModel.getTextoHorasDebitoOuCredito());


        parametrosRelatorio.put("pontosDataSource", relatorioModel.getPontosDataSource());

        parametrosRelatorio.put("nome", usuario.getNome());
        parametrosRelatorio.put("cargo", usuario.getCargo());
        parametrosRelatorio.put("funcao", usuario.getFuncao());
        parametrosRelatorio.put("matricula", "RR" + usuario.getMatricula());
        parametrosRelatorio.put("lotacao", usuario.getLotacao());
        parametrosRelatorio.put("periodo", dataParaString(inicio, PADRAO_DATA) + " a " + dataParaString(fim, PADRAO_DATA));

        var streamRelatorioPonto = new ByteArrayInputStream(relatorioPonto.orElseThrow().getConteudo());
        var printRelatorioPonto = fillReport(streamRelatorioPonto, parametrosRelatorio, new JREmptyDataSource());
        return exportReportToPdf(printRelatorioPonto);

    }
}
