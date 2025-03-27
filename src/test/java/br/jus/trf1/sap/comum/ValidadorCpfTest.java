package br.jus.trf1.sap.comum;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static br.jus.trf1.sap.comum.ValidadorCpf.*;
import static org.junit.jupiter.api.Assertions.*;

class ValidadorCpfTest {

    @Test
    void isCPFTeste() {
        var cpfs = new ArrayList<String>();
        var cpfParcial = "77643070";

        for (int i = 0; i < 999; i++) {
            var cpf = "";
            if (i < 10) {
                cpf = cpfParcial + "0" + i;
                if (isCPF(cpf)) {
                    cpfs.add(cpf);
                }
            }else{
                cpf = cpfParcial + i;
                if (isCPF(cpf)) {
                    cpfs.add(cpf);
                }
            }
        }

        for (String cpf : cpfs) {
            System.out.println(cpf);
        }

        assertTrue(isCPF("77643070253"));
    }


}