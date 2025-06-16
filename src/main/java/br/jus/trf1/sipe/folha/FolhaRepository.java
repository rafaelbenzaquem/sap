package br.jus.trf1.sipe.folha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FolhaRepository extends JpaRepository<Folha, Long> {

    @Query("SELECT f FROM Folha f WHERE f.servidor.matricula = :matricula AND f.mes = :mes AND f.ano =:ano")
    Optional<Folha> buscarFolha(@Param("matricula") String matricula, @Param("mes") Integer mes, @Param("ano") Integer ano);

}
