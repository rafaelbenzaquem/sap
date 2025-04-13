package br.jus.trf1.sipe.comum.util;

import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.ponto.web.PontoReadController;
import br.jus.trf1.sipe.ponto.web.dto.PontoNovoResponse;
import br.jus.trf1.sipe.registro.Registro;
import br.jus.trf1.sipe.registro.web.RegistroReadController;
import br.jus.trf1.sipe.registro.web.dto.RegistroResponse;
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
        var matricula = registros.getFirst().getPonto().getId().getMatricula();
        var dia = registros.getFirst().getPonto().getId().getDia();

        var registrosEntityModelList = registros.stream().map(registro ->
                EntityModel.of(
                        RegistroResponse.of(registro),
                        linkTo(methodOn(RegistroReadController.class).buscaRegistro(registro.getId())).withSelfRel()
                )
        ).toList();

        return CollectionModel.of(registrosEntityModelList,
                linkTo(methodOn(RegistroReadController.class).listarRegistrosDoPonto(matricula, dia)).withSelfRel()
        );
    }

    public static CollectionModel<EntityModel<PontoNovoResponse>> addLinksHATEOAS(LocalDate inicio, LocalDate fim, List<Ponto> pontos) {
        Objects.requireNonNull(pontos);
        if (pontos.isEmpty()) {
            return CollectionModel.empty();
        }
        var matricula = pontos.getFirst().getId().getMatricula();

        var registrosEntityModelList = pontos.stream().map(ponto ->
                EntityModel.of(
                        PontoNovoResponse.of(ponto),
                        linkTo(methodOn(PontoReadController.class).buscaPonto(ponto.getId().getMatricula(),
                                ponto.getId().getDia())).withSelfRel()
                )
        ).toList();

        return CollectionModel.of(registrosEntityModelList,
                linkTo(methodOn(PontoReadController.class).buscaPontosPorIntervalosDatas(matricula, inicio, fim)).withSelfRel()
        );
    }

}
