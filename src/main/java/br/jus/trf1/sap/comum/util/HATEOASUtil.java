package br.jus.trf1.sap.comum.util;

import br.jus.trf1.sap.registro.Registro;
import br.jus.trf1.sap.registro.web.RegistroReadController;
import br.jus.trf1.sap.registro.web.dto.RegistroResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class HATEOASUtil {

    private HATEOASUtil() {
    }

    public static CollectionModel<EntityModel<RegistroResponse>> addLinksHATEOAS(List<Registro> registros){
        Objects.requireNonNull(registros);
        if(registros.isEmpty()){
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

}
