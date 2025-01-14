package br.jus.trf1.sap.vinculo;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VinculoRepository extends JpaRepository<Vinculo, Integer> {

    Optional<Vinculo> findVinculoByCracha(String cracha);

    Optional<Vinculo> findVinculoByNome(String nome);

    Optional<Vinculo> findVinculoByNomeOrCracha(String nome, String cracha);

    Optional<Vinculo> findVinculoByMatricula(Integer matricula);
}
