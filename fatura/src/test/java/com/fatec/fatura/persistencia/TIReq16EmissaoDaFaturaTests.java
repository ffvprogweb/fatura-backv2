package com.fatec.fatura.persistencia;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.fatura.model.Fatura;
import com.fatec.fatura.service.IFaturaRepository;
@SpringBootTest
class TIReq16EmissaoDaFaturaTests {
	Logger logger = LogManager.getLogger(this.getClass());
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	Fatura fatura;
	@Autowired
	IFaturaRepository repository;
	@Test
	void ct01_quando_dados_validos_registra_fatura_com_sucesso() {
		LocalDate dataVencimento = LocalDate.parse("02/10/2026", formatter);
		fatura = new Fatura("39086360009", dataVencimento, "moveis planejados", "1000.50");
		repository.save(fatura);
		assertTrue(repository.count() >= 1);
		List<Fatura> fatura = repository.findByCpfAndDataEmissao("39086360009", LocalDate.now());
		assertFalse(fatura.isEmpty());
	}

}
