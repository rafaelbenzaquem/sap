package br.jus.trf1.sap.arquivo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArquivoRepository extends JpaRepository<Arquivo, Integer> {

    Optional<Arquivo> findByNome(String nome);

}
