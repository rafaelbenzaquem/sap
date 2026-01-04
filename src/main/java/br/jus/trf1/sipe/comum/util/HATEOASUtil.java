package br.jus.trf1.sipe.comum.util;

import br.jus.trf1.sipe.ponto.application.web.PontoReadController;
import br.jus.trf1.sipe.ponto.application.web.PontoWebMapper;
import br.jus.trf1.sipe.ponto.application.web.dto.PontoNovoResponse;
import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.registro.application.web.RegistroReadController;
import br.jus.trf1.sipe.registro.application.web.dto.RegistroResponse;
import br.jus.trf1.sipe.registro.domain.model.Registro;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class HATEOASUtil {

    private HATEOASUtil() {
    }

    public static CollectionModel<EntityModel<RegistroResponse>> addLinksHATEOAS(List<Registro> registros) {
        Objects.requireNonNull(registros);
        if (registros.isEmpty()) {
            return CollectionModel.empty();
        }
        var matricula = registros.getFirst().getPonto().getId().getUsuario().getMatricula();
        var dia = registros.getFirst().getPonto().getId().getDia();

        var registrosEntityModelList = registros.stream().map(registro ->
                EntityModel.of(
                        RegistroResponse.of(registro),
                        linkTo(methodOn(RegistroReadController.class).buscaRegistro(registro.getId())).withSelfRel()
                )
        ).toList();

        return CollectionModel.of(registrosEntityModelList,
                linkTo(methodOn(RegistroReadController.class).listarRegistrosDoPonto(matricula, dia, false)).withSelfRel()
        );
    }

    public static CollectionModel<EntityModel<PontoNovoResponse>> addLinksHATEOAS(LocalDate inicio, LocalDate fim, boolean pendente, List<Ponto> pontos) {
        Objects.requireNonNull(pontos);
        if (pontos.isEmpty()) {
            return CollectionModel.empty();
        }
        var matricula = pontos.getFirst().getId().getUsuario().getMatricula();

        var registrosEntityModelList = pontos.stream().map(ponto ->
                EntityModel.of(
                        PontoWebMapper.toNovoResponse(ponto),
                        linkTo(methodOn(PontoReadController.class).buscaPonto(ponto.getId().getUsuario().getMatricula(),
                                ponto.getId().getDia())).withSelfRel()
                )
        ).toList();

        return CollectionModel.of(registrosEntityModelList,
                linkTo(methodOn(PontoReadController.class).buscaPontosPorIntervalosDatas(matricula, inicio, fim, pendente)).withSelfRel()
        );
    }

}
