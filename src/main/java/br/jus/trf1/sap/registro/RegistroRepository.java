package br.jus.trf1.sap.registro;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistroRepository extends JpaRepository<Registro, Long> {

    Optional<Registro> findByCodigoAcesso(Integer codigoAcesso);

}
