package br.jus.trf1.sipe.servidor;

import br.jus.trf1.sipe.ausencia.Ausencia;
import br.jus.trf1.sipe.lotacao.LotacaoMapping;
import br.jus.trf1.sipe.servidor.aplication.jsarh.ServidorJSarh;
import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import br.jus.trf1.sipe.servidor.infrastructure.persistence.ServidorJpa;
import br.jus.trf1.sipe.usuario.infrastructure.persistence.UsuarioJpa;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ServidorMapper {

    private ServidorMapper() {
    }

    public static ServidorJpa toEntity(Servidor servidor) {
        return  ServidorJpa.builder()
                .id(servidor.getId())
                .nome(servidor.getNome())
                .matricula(servidor.getMatricula())
                .cracha(servidor.getCracha())
                .horaDiaria(servidor.getHoraDiaria())
                .email(servidor.getEmail())
                .funcao(servidor.getFuncao())
                .cargo(servidor.getCargo())
                .lotacao(servidor.getLotacao())
                .build();
    }

    public static Servidor toDomain(ServidorJpa servidorJpa) {
        return  Servidor.builder()
                .id(servidorJpa.getId())
                .nome(servidorJpa.getNome())
                .matricula(servidorJpa.getMatricula())
                .cracha(servidorJpa.getCracha())
                .horaDiaria(servidorJpa.getHoraDiaria())
                .email(servidorJpa.getEmail())
                .funcao(servidorJpa.getFuncao())
                .cargo(servidorJpa.getCargo())
                .lotacao(servidorJpa.getLotacao())
                .build();
    }

}
