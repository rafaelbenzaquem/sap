package br.jus.trf1.sap.ponto;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class PontoId {

   private Integer matricula;
   private LocalDate dia;

}
