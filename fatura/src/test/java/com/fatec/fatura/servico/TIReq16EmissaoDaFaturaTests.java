package com.fatec.fatura.servico;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
		assertTrue(repository.count()>=1);
		List<Fatura> f = servico.consultaCpf("21805801007");
		assertTrue (f.size() >=1 );
	}

}
