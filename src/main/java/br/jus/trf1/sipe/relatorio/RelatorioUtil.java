package br.jus.trf1.sipe.relatorio;

import br.jus.trf1.sipe.arquivo.db.Arquivo;
import br.jus.trf1.sipe.externo.jsarh.feriado.FeriadoExternal;
import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.relatorio.model.RelatorioPontoData;
import br.jus.trf1.sipe.relatorio.model.UsuarioRelatorioPontoModel;
import br.jus.trf1.sipe.servidor.Servidor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static br.jus.trf1.sipe.relatorio.model.util.FomatadorTextoUtil.formataTextoPeriodo;

public class RelatorioUtil {


    public RelatorioUtil() {
    }

    public static HashMap<String, Object> mapeandoParametrosRelatorio(RelatorioPontoData relatorioModel,
                                                                      Arquivo arquivoLogoImagemEsquerdaSuperior,
                                                                      Arquivo arquivoLogoImagemDireitaSuperior,
                                                                      LocalDate inicio,
                                                                      LocalDate fim) {
        var parametrosRelatorio = new HashMap<String, Object>();


        parametrosRelatorio.put("logoImagem", arquivoLogoImagemEsquerdaSuperior.getBytes());
        parametrosRelatorio.put("logoImagem2", arquivoLogoImagemDireitaSuperior.getBytes());
        parametrosRelatorio.put("obs", "Sem observações...");
        parametrosRelatorio.put("totalHorasUteis", relatorioModel.getTextoHorasUteis());
        parametrosRelatorio.put("totalHorasPermanencia", relatorioModel.getTextoPermanenciaTotal());
        parametrosRelatorio.put("creditoDebitoLabel", relatorioModel.getRotuloHorasCreditoOuDebito());
        parametrosRelatorio.put("horaCDTotal", relatorioModel.getTextoHorasCreditoOuDebito());

        parametrosRelatorio.put("pontosDataSource", relatorioModel.getPontosDataSource());


        var usuario = relatorioModel.getUsuario();

        parametrosRelatorio.put("nome", usuario.nome());
        parametrosRelatorio.put("cargo", usuario.cargo());
        parametrosRelatorio.put("funcao", usuario.funcao());
        parametrosRelatorio.put("matricula", usuario.matricula());
        parametrosRelatorio.put("lotacao", usuario.lotacao());
        parametrosRelatorio.put("periodo", formataTextoPeriodo(inicio, fim));
        return parametrosRelatorio;
    }

    public static RelatorioPontoData processaDadosServidorParaRelatorio(Servidor servidor, List<Ponto> pontos, List<FeriadoExternal> feriados) {
        var usuario = UsuarioRelatorioPontoModel.builder()
                .nome(servidor.getNome())
                .cargo(servidor.getCargo() == null ? "Servidor Requisitado" : servidor.getCargo())
                .funcao(servidor.getFuncao())
                .lotacao(servidor.getLotacao().getDescricao())
                .matricula(servidor.getMatricula())
                .horasDiaria(servidor.getHoraDiaria())
                .ausencias(servidor.getAusencias())
                .build();

        return new RelatorioPontoData(usuario, pontos, feriados);
    }


}
