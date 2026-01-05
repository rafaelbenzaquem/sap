package br.jus.trf1.sipe.registro.domain.port.out;

import br.jus.trf1.sipe.registro.domain.model.Registro;

import java.time.LocalDate;
import java.util.List;

public interface RegistroPersistencePort {

    Registro buscaPorId(Long id);

    Boolean existe(Long id);

    List<Registro> listaAtuaisDoPonto(String matricula, LocalDate dia, boolean apenasAtivos);

    List<Registro> salvaTodos(List<Registro> registros);

    List<Registro> listaRegistrosProvenientesDoSistemaExternoPorPonto(String matricula, LocalDate dia);

    Registro salva(Registro registro);

    void apagarPorId(Long idRegistro);

}
