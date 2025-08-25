package com.fatec.fatura.servico;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.fatura.model.Fatura;
import com.fatec.fatura.model.FaturaDto;
import com.fatec.fatura.service.FaturaResponse;
import com.fatec.fatura.service.IFaturaRepository;
import com.fatec.fatura.service.IFaturaService;

@SpringBootTest
class TIReq16EmissaoDaFaturaTests {
	@Autowired
	IFaturaService servico;
	@Autowired
	IFaturaRepository repository;

	@Test
	void ct01_quando_dados_validos_retorna_sucesso() {
		FaturaDto fatura = new FaturaDto("21805801007", "02/10/2025", "moveis planejados", "1000.50");
		FaturaResponse response = servico.registrar(fatura);
		System.out.println(">>>>>> CT01 = " + response.getMensagem());
		assertTrue(repository.count() >= 1);
		List<Fatura> f = servico.consultaFaturaPorCpf("21805801007");
		assertTrue(f.size() >= 1);
	}

	@Test
	void ct02_quando_cpf_invalido_vazio_mensagem_de_erro()  {
		try {
			FaturaDto fatura = new FaturaDto("", "02/10/2025", "moveis planejados", "1000.50");
			FaturaResponse response = servico.registrar(fatura);
			System.out.println(">>>>>> CT02 = " + response.getMensagem());
			assertEquals("Erro no registro da fatura", response.getMensagem());
		} catch (Exception e) {
			fail("nao deveria falhar por exception =>" + e.getMessage());
		}
	}
	@Test
	void ct03_quando_cpf_alfa_numerico_mensagem_de_erro()  {
		try {
			FaturaDto fatura = new FaturaDto("a1", "02/10/2025", "moveis planejados", "1000.50");
			FaturaResponse response = servico.registrar(fatura);
			System.out.println(">>>>>> CT02 = " + response.getMensagem());
			assertEquals("Erro no registro da fatura", response.getMensagem());
		} catch (Exception e) {
			fail("nao deveria falhar por exception =>" + e.getMessage());
		}
	}
	@Test
	void ct04_quando_cpf_null_mensagem_de_erro() {
		try {
			FaturaDto fatura = new FaturaDto(null, "02/10/2025", "moveis planejados", "1000.50");
			FaturaResponse response = servico.registrar(fatura);
			System.out.println(">>>>>> CT03 = " + response.getMensagem());
			assertEquals("Erro no registro da fatura", response.getMensagem());
		} catch (Exception e) {
			fail("nao deveria falhar por exception =>" + e.getMessage());
		}
	}
	@Test
	void ct05_registrar_fatura_com_data_invalida_fev() {

		FaturaDto fatura = new FaturaDto("21805801007", "29/02/2026", "moveis planejados", "100");
		FaturaResponse response = servico.registrar(fatura);
		System.out.println(">>>>>> CT07 data ajustada => " + response.getFatura().getDataVencimento().toString());
		assertTrue(response.isSucesso());
	}

	@Test
	void ct06_registrar_fatura_com_data_invalida_fev() {

		FaturaDto fatura = new FaturaDto("21805801007", "31/02/2026", "moveis planejados", "100");
		FaturaResponse response = servico.registrar(fatura);
		System.out.println(">>>>>> CT08 data ajustada => " + response.getFatura().getDataVencimento().toString());
		assertTrue(response.isSucesso());
	}

	@Test
	void ct07_registrar_fatura_com_data_invalida_fev() {

		FaturaDto fatura = new FaturaDto("21805801007", "25/08/2025", "moveis planejados", "100");
		FaturaResponse response = servico.registrar(fatura);
		assertTrue(response.isSucesso());
	}
	@Test
	void ct08_registrar_fatura_com_valor_invalido() {
		try {
			FaturaDto fatura = new FaturaDto("21805801007", "02/10/2025", "moveis planejados", "0");
			FaturaResponse response = servico.registrar(fatura);
			assertEquals("Erro no registro da fatura", response.getMensagem());
		} catch (Exception e) {
			fail("nao deveria falhar por exception =>" + e.getMessage());
		}
	}

	@Test
	void ct09_registrar_fatura_com_valor_invalido_negativo() {
		try {
			FaturaDto fatura = new FaturaDto("21805801007", "02/10/2025", "moveis planejados", "-1");
			FaturaResponse response = servico.registrar(fatura);
			assertEquals("Erro no registro da fatura", response.getMensagem());
		} catch (Exception e) {
			fail("nao deveria falhar por exception =>" + e.getMessage());
		}
	}

	@Test
	void ct10_registrar_fatura_com_valor_invalido_caracter() {
		try {
			FaturaDto fatura = new FaturaDto("21805801007", "02/10/2025", "moveis planejados", "a1");
			FaturaResponse response = servico.registrar(fatura);
			assertEquals("Erro no registro da fatura", response.getMensagem());
		} catch (Exception e) {
			fail("nao deveria falhar por exception =>" + e.getMessage());
		}
	}

	
	/**
	 * valida o comportamento da configuracao strict
	 */
	@Test
	void ct11_registrar_fatura_com_data_invalida_fev() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withResolverStyle(ResolverStyle.STRICT);
		try {
			LocalDate dataInvalida = LocalDate.parse("31/02/2026", formatter);
			System.err.println("Data parseada => " + dataInvalida.toString());
		} catch (DateTimeParseException e) {
			System.err.println(">>>>>> CT10 exception valida o comportamento da config STRICT: A data '"
					+ e.getParsedString() + "' é inválida.");
			System.err.println(">>>>>> Mensagem da exceção: " + e.getMessage());
		}
	}
}
