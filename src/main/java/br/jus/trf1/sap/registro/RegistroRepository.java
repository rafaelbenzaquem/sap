package br.jus.trf1.sap.registro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RegistroRepository extends JpaRepository<Registro, Long> {

    @Query("""
            select r from Registro r where r.ponto.id.matricula =:matricula and
            r.ponto.id.dia =:dia
            """)
    List<Registro> listarRegistrosPonto(@Param("matricula")String matricula,
                                        @Param("dia")LocalDate dia);


    Optional<Registro> findByCodigoAcesso(Integer codigoAcesso);

}
