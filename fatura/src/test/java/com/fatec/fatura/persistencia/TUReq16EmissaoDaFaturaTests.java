package com.fatec.fatura.persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.fatec.fatura.Fatura;

class TUReq16EmissaoDaFaturaTests {
	Logger logger = LogManager.getLogger(this.getClass());
	Fatura fatura;
	@Test
	void ct01_quando_dados_validos_fatura_nao_eh_nulo() {
		try {
			// dado que as informacoes de fatura sao validas
			// quando confirmo a fatura
			fatura = new Fatura("39086360009", "02/10/2026", "moveis planejados", "1000.50");
			// entao fatura é registrada com data de emisssao igual a data de hoje
			assertNotNull(fatura);
			assertFalse(fatura.isCancelada());
			assertFalse(fatura.isPaga());
		} catch (Exception e) {
			logger.info(">>>>>> ct01 - nao deveria falhar => " + e.getMessage());
			fail("nao deveria falhar fatura valida");

		}
	}
	@Test
	void ct02_quando_cnpj_invalido_vazio_mensagem_de_erro() {
		try {
			// dado que que o cnpj é vazio
			// quando confirmo a fatura
			fatura = new Fatura("", "02/10/2026", "moveis planejados", "1000.50");
			fail("deveria falhar fatura invalida");
		} catch (Exception e) {
			// entao retorna mensagem de cnpj invalido
			logger.info(">>>>>> ct02 erro=> " + e.getMessage());
			assertEquals("CPF invalido", e.getMessage());

		}
	}
	@Test
	void ct03_quando_cnpj_invalido_formato_mensagem_de_erro() {
		try {
			// dado que que o cnpj é vazio
			// quando confirmo a fatura
			fatura = new Fatura("06615406004", "02/10/2026", "moveis planejados", "1000.50");

		} catch (Exception e) {
			// entao retorna mensagem de cnpj invalido
			logger.info(">>>>>> ct03 erro=> " + e.getMessage());
			assertEquals("CPF invalido", e.getMessage());

		}
	}
	@Test
	void ct04_quando_valor_invalido_entao_retorna_msg_erro() {
		try {
			// dado que que o valor eh invalido
			// quando instancio o objeto
			fatura = new Fatura("06615406004", "02/10/2026", "moveis planejados", "1x500.00");
			fail("deveria falhar com valor invalido");
		} catch (Exception e) {
			// entao retorna mensagem de erro
			assertEquals("Valor da fatura invalido", e.getMessage());
		}

	}
}
