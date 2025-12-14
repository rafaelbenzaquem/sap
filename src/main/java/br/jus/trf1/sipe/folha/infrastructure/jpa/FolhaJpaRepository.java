package br.jus.trf1.sipe.folha.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FolhaJpaRepository extends JpaRepository<FolhaJpa, FolhaJpaId> {

    @Query("SELECT f FROM FolhaJpa f WHERE f.id.servidor.matricula = :matricula AND f.id.mes = :mes AND f.id.ano =:ano")
    Optional<FolhaJpa> buscarFolha(@Param("matricula") String matricula, @Param("mes") Integer mes, @Param("ano") Integer ano);

}
