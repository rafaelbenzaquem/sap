package br.jus.trf1.sap.ponto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistroRepository extends JpaRepository<Registro,Long> {

    List<Registro> findByPonto(Ponto ponto);

}
