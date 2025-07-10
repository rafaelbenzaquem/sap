package br.jus.trf1.sipe.servidor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServidorRepository extends JpaRepository<Servidor, Integer> {


     Optional<Servidor> findByMatricula(String matricula);

}
