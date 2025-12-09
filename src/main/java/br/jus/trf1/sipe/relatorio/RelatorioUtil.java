package br.jus.trf1.sipe.relatorio;

import br.jus.trf1.sipe.arquivo.db.Arquivo;
import br.jus.trf1.sipe.ausencia.Ausencia;
import br.jus.trf1.sipe.feriado.externo.jsarh.FeriadoJSarh;
import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.relatorio.model.PontoModel;
import br.jus.trf1.sipe.relatorio.model.RelatorioModel;
import br.jus.trf1.sipe.relatorio.model.UsuarioModel;
import br.jus.trf1.sipe.servidor.infrastructure.persistence.ServidorJpa;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.jus.trf1.sipe.comum.util.FomatadorTextoUtil.formataTextoPeriodo;

public class RelatorioUtil {


    private RelatorioUtil() {
    }

    public static Map<String, Object> mapeandoParametrosRelatorio(RelatorioModel relatorioModel,
                                                                  Arquivo arquivoLogoImagemEsquerdaSuperior,
                                                                  Arquivo arquivoLogoImagemDireitaSuperior,
                                                                  LocalDate inicio,
                                                                  LocalDate fim,
                                                                  JRBeanCollectionDataSource dataSource) {
        var parametrosRelatorio = new HashMap<String, Object>();


        parametrosRelatorio.put("logoImagem", arquivoLogoImagemEsquerdaSuperior.getBytes());
        parametrosRelatorio.put("logoImagem2", arquivoLogoImagemDireitaSuperior.getBytes());
        parametrosRelatorio.put("obs", "Sem observações...");
        parametrosRelatorio.put("totalHorasUteis", relatorioModel.getTextoHorasUteis());
        parametrosRelatorio.put("totalHorasPermanencia", relatorioModel.getTextoPermanenciaTotal());
        parametrosRelatorio.put("creditoDebitoLabel", relatorioModel.getRotuloHorasCreditoOuDebito());
        parametrosRelatorio.put("horaCDTotal", relatorioModel.getTextoHorasCreditoOuDebito());

        parametrosRelatorio.put("pontosDataSource", dataSource);


        var usuario = relatorioModel.getUsuario();

        parametrosRelatorio.put("nome", usuario.getNome());
        parametrosRelatorio.put("cargo", usuario.getCargo());
        parametrosRelatorio.put("funcao", usuario.getFuncao());
        parametrosRelatorio.put("matricula", usuario.getMatricula());
        parametrosRelatorio.put("lotacao", usuario.getLotacao());
        parametrosRelatorio.put("periodo", formataTextoPeriodo(inicio, fim));
        return parametrosRelatorio;
    }

    public static RelatorioModel processaDadosServidorParaRelatorio(ServidorJpa servidor, List<Ponto> pontos, List<FeriadoJSarh> feriados) {
        var usuario = UsuarioModel.builder()
                .nome(servidor.getNome())
                .cargo(servidor.getCargo() == null ? "Servidor Requisitado" : servidor.getCargo())
                .funcao(servidor.getFuncao())
                .lotacao(servidor.getLotacao().getDescricao())
                .matricula(servidor.getMatricula())
                .horasDiaria(servidor.getHoraDiaria())
                .ausencias(servidor.getAusencias())
                .build();

        return new RelatorioModel(usuario, pontos, feriados);
    }


    public static List<PontoModel> carregarDadosPontos(List<Ponto> pontos, List<FeriadoJSarh> feriados, List<Ausencia> ausencia) {
        return pontos.stream()
                .map(ponto -> {

                    // Verifica se a data do ponto está dentro de um período de ausência
                    Ausencia ausenciaCorrespondente = ausencia.stream()
                            .filter(a -> !ponto.getId().getDia().isBefore(a.getInicio()) &&
                                    !ponto.getId().getDia().isAfter(a.getFim()))
                            .findFirst()
                            .orElse(null);
                    // Verifica se a data do ponto é um feriado
                    FeriadoJSarh feriadoCorrespondente = feriados.stream()
                            .filter(feriado -> feriado.getData().equals(ponto.getId().getDia()))
                            .findFirst()
                            .orElse(null);

                    var descricao = feriadoCorrespondente == null ? null : feriadoCorrespondente.getDescricao();
                    descricao = ausenciaCorrespondente == null ? descricao : ausenciaCorrespondente.getDescricao();

                    return descricao == null ? new PontoModel(ponto) :
                            new PontoModel(ponto, descricao);
                }).toList();
    }

}
