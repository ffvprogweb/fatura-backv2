package com.fatec.fatura.persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.fatec.fatura.model.Fatura;

class TUReq16EmissaoDaFaturaTests {
	Logger logger = LogManager.getLogger(this.getClass());
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	Fatura fatura;

	@Test
	void ct01_quando_dados_validos_retorna_sucesso() {
		try {
			// dado que as informacoes de fatura sao validas
			LocalDate dataVencimento = LocalDate.parse("02/10/2026", formatter);
			fatura = new Fatura("39086360009", dataVencimento, "moveis planejados", "1000.50");
			// quando confirmo a fatura
			// entao a emissão da fatura é realizada com sucesso
			assertNotNull(fatura);
		} catch (Exception e) {
			logger.info(">>>>>> ct01 mensagem da exception => " + e.getMessage());
			fail("nao deveria falhar fatura valida");
		}
	}

	@Test
	void ct02_quando_cpf_invalido_vazio_mensagem_de_erro() {
		try {
			// dado que que o cnpj é vazio
			// quando confirmo a fatura
			LocalDate dataVencimento = LocalDate.parse("02/10/2026", formatter);
			fatura = new Fatura("", dataVencimento, "moveis planejados", "1000.50");
			fail("deveria falhar fatura invalida");
		} catch (Exception e) {
			// entao retorna mensagem de cnpj invalido
			assertEquals("CPF invalido", e.getMessage());

		}
	}
	@Test
	void ct03_quando_cpf_alfa_numerico_mensagem_de_erro() {
		try {
			// dado que que o cnpj é letra
			// quando confirmo a fatura
			LocalDate dataVencimento = LocalDate.parse("02/10/2026", formatter);
			fatura = new Fatura("a1", dataVencimento, "moveis planejados", "1000.50");
			fail("deveria falhar fatura invalida");
		} catch (Exception e) {
			// entao retorna mensagem de cnpj invalido
			assertEquals("CPF invalido", e.getMessage());

		}
	}
	
	@Test
	void ct04_quando_cpf_null_mensagem_de_erro() {
		try {
			// dado que que o cnpj é letra
			// quando confirmo a fatura
			LocalDate dataVencimento = LocalDate.parse("02/10/2026", formatter);
			fatura = new Fatura(null, dataVencimento, "moveis planejados", "1000.50");
			fail("deveria falhar fatura invalida");
		} catch (Exception e) {
			// entao retorna mensagem de cnpj invalido
			assertEquals("CPF invalido", e.getMessage());

		}
	}
	@Test
	void ct05_quando_data_vencimento_domingo_retorna_invalido() {
		try {
			LocalDate dataVencimento = LocalDate.parse("31/08/2025", formatter);
			fatura = new Fatura("39086360009", dataVencimento, "moveis planejados", "1000.50");
			fail("deveria falhar fatura data invalida domingo");
		} catch (Exception e) {
			assertEquals("Data de vencimento: formato invalido ou domingo ou menor que data atual", e.getMessage());
		}
	}

	@Test
	void ct06_quando_data_vencimento_invalida_menor_data_atual() {
		try {
			LocalDate dataVencimento = LocalDate.parse("31/08/2024", formatter);
			fatura = new Fatura("39086360009", dataVencimento, "moveis planejados", "1000.50");
			fail("deveria falhar fatura data invalida");
		} catch (Exception e) {
			assertEquals("Data de vencimento: formato invalido ou domingo ou menor que data atual", e.getMessage());

		}
	}
	/**
	 * validacao de data semantica 31/02/2026
	 */
	@Test
	void ct07_quando_data_vencimento_invalido_fevereiro31_msg_erro() {
		String dataString = "31/02/2026";
		 try {
			    // supoe que a data esta formatada corretamente
			    // realizar o parse primeiro para validar o formato
	            // Divide a string em partes
	            String[] partes = dataString.split("/");
	            
	            // Converte as partes para inteiros
	            int dia = Integer.parseInt(partes[0]);
	            int mes = Integer.parseInt(partes[1]);
	            int ano = Integer.parseInt(partes[2]);

	            // Tenta criar a data com LocalDate.of(), que valida a data
	            LocalDate dataValida = LocalDate.of(ano, mes, dia);
	            System.out.println("Data válida: " + dataValida);
	            
	        } catch (DateTimeException e) {
	            System.err.println("Erro de validação: A data é semanticamente inválida. " + e.getMessage());
	        } catch (NumberFormatException e) {
	            System.err.println("Erro de formato: A string da data contém caracteres não numéricos.");
	        } catch (ArrayIndexOutOfBoundsException e) {
	            System.err.println("Erro de formato: A string da data não está no formato dd/MM/yyyy.");
	        }
		
	}
	@Test
	void ct08_quando_data_vencimento_invalido_feveriro31_msg_erro() {
		try {
            // Tentativa de criar uma data inválida
            LocalDate dataInvalida = LocalDate.of(2023, 2, 31);
            System.out.println("Data válida: " + dataInvalida.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        } catch (DateTimeParseException e) {
            System.out.println("Erro: Data inválida - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ocorreu um erro: " + e.getMessage());
        }
		
	}
	@Test
	void ct09_quando_data_vencimento_invalido_fevereiro31_msg_erro() {
	    // O teste espera que a exceção DateTimeException seja lançada
	    assertThrows(DateTimeException.class, () -> {
	        // Código que deve lançar a exceção
	        LocalDate.of(2026, 2, 31);
	    });
	}
	@Test
	void ct10_quando_data_vencimento_formato() {
		try {
			LocalDate dataVencimento = LocalDate.parse("31-08-2024", formatter);
		} catch (Exception e) {
			assertEquals("Text '31-08-2024' could not be parsed at index 2", e.getMessage());

		}
	}
	@Test
	void ct11_quando_valor_invalido_vazio() {
		try {
			LocalDate dataVencimento = LocalDate.parse("02/10/2026", formatter);
			fatura = new Fatura("39086360009", dataVencimento, "moveis planejados", "");
		} catch (Exception e) {
			assertEquals("Valor da fatura invalido", e.getMessage());

		}
	}
	@Test
	void ct12_quando_valor_invalido_branco() {
		try {
			LocalDate dataVencimento = LocalDate.parse("02/10/2026", formatter);
			fatura = new Fatura("39086360009", dataVencimento, "moveis planejados", " ");
		} catch (Exception e) {
			assertEquals("Valor da fatura invalido", e.getMessage());

		}
	}
	@Test
	void ct13_quando_valor_invalido_caracter() {
		try {
			LocalDate dataVencimento = LocalDate.parse("02/10/2026", formatter);
			fatura = new Fatura("39086360009", dataVencimento, "moveis planejados", "s");
		} catch (Exception e) {
			assertEquals("Valor da fatura invalido", e.getMessage());

		}
	}
	@Test
	void ct14_quando_valor_invalido_menor_que_zero() {
		try {
			LocalDate dataVencimento = LocalDate.parse("02/10/2026", formatter);
			fatura = new Fatura("39086360009", dataVencimento, "moveis planejados", "-1");
		} catch (Exception e) {
			assertEquals("O valor da fatura deve ser maior que zero.", e.getMessage());

		}
	}
}
