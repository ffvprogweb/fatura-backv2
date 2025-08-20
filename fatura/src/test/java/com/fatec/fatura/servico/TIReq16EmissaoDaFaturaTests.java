package com.fatec.fatura.servico;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
	void ct01_registrar_fatura_com_sucesso() {
		FaturaDto fatura = new FaturaDto("21805801007", "02/10/2025", "moveis planejados", "1000.50");
		FaturaResponse response = servico.registrar(fatura);
		System.out.println(response.getMensagem());
		assertTrue(repository.count() >= 1);
		List<Fatura> f = servico.consultaCpf("21805801007");
		assertTrue(f.size() >= 1);
	}

	@Test
	void ct02_registrar_fatura_com_cpf_invalido_branco() {
		try {
			FaturaDto fatura = new FaturaDto("", "02/10/2025", "moveis planejados", "1000.50");
			FaturaResponse response = servico.registrar(fatura);
			assertEquals("Erro no registro da fatura", response.getMensagem());
		} catch (Exception e) {
			fail("nao deveria falhar por exception =>" + e.getMessage());
		}

	}
	@Test
	void ct03_registrar_fatura_com_cpf_invalido_null() {
		try {
			FaturaDto fatura = new FaturaDto(null, "02/10/2025", "moveis planejados", "1000.50");
			FaturaResponse response = servico.registrar(fatura);
			assertEquals("Erro no registro da fatura", response.getMensagem());
		} catch (Exception e) {
			fail("nao deveria falhar por exception =>" + e.getMessage());
		}

	}
	@Test
	void ct04_registrar_fatura_com_valor_invalido() {
		try {
			FaturaDto fatura = new FaturaDto("21805801007", "02/10/2025", "moveis planejados", "0");
			FaturaResponse response = servico.registrar(fatura);
			assertEquals("Erro no registro da fatura", response.getMensagem());
		} catch (Exception e) {
			fail("nao deveria falhar por exception =>" + e.getMessage());
		}

	}
	@Test
	void ct05_registrar_fatura_com_valor_invalido() {
		try {
			FaturaDto fatura = new FaturaDto("21805801007", "02/10/2025", "moveis planejados", "-1");
			FaturaResponse response = servico.registrar(fatura);
			assertEquals("Erro no registro da fatura", response.getMensagem());
		} catch (Exception e) {
			fail("nao deveria falhar por exception =>" + e.getMessage());
		}

	}
	@Test
	void ct06_registrar_fatura_com_valor_invalido() {
		try {
			FaturaDto fatura = new FaturaDto("21805801007", "02/10/2025", "moveis planejados", "a1");
			FaturaResponse response = servico.registrar(fatura);
			assertEquals("Erro no registro da fatura", response.getMensagem());
		} catch (Exception e) {
			fail("nao deveria falhar por exception =>" + e.getMessage());
		}

	}
}
