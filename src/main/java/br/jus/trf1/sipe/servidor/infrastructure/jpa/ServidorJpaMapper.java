package br.jus.trf1.sipe.servidor.infrastructure.jpa;

import br.jus.trf1.sipe.lotacao.infrastructure.persistence.LotacaoJpaMapper;
import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServidorJpaMapper {

    private ServidorJpaMapper() {
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
                .lotacao(LotacaoJpaMapper.toEntity(servidor.getLotacao()))
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
                .lotacao(LotacaoJpaMapper.toDomain(servidorJpa.getLotacao()))
                .build();
    }

}
