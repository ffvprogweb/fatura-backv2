package com.fatec.fatura.persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.fatec.fatura.model.Fatura;
@DataJpaTest
class TUReq16EmissaoDaFaturaDDTests {

	CsvReader leitor;
	List<FaturaMassaDeTeste> d;
	Fatura fatura;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	@Test
	public void ct_verifica_comportamento_fatura() {
		String path = "C:/edson/dataset_fatura/fatura2.csv";
		//String path = "e:/dataset_fatura/fatura2.csv";
		String resultadoEsperado = "";
		try {
			
			d = CsvReader.lerArquivo(path);
			System.out.println(">>>>>>>>> quantidade de registros =>" + d.size());
		} catch (IOException e) {
			System.out.println(">>>>>> Erro de IO => " + e.getMessage());
		}
		int registro = 1;
		for (FaturaMassaDeTeste f : d) {

			try {
				System.out.println(">>>>>> registro => " + registro);
				resultadoEsperado = f.re();
				System.out.println("cpf ==> " + f.cpf() + " - RE= " + resultadoEsperado);
				LocalDate dataVencimento = LocalDate.parse(f.dtemissao(), formatter);
				fatura = new Fatura(f.cpf(), dataVencimento, f.servico(), f.valor());
				assertNotNull(fatura);
				assertEquals(resultadoEsperado, "satisfatório");

			} catch (Exception e) {
				System.out.println(">>>>>> classe invalida resultado esperado =>" + resultadoEsperado
						+ "- resultado obtido =>" + e.getMessage());
				assertEquals(resultadoEsperado, e.getMessage());
			}
			registro = registro + 1;
		}
	}

}
