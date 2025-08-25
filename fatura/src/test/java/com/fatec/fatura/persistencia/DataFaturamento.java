package com.fatec.fatura.persistencia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataFaturamento {
	public String obtemDataAtual() {
		// 1. Obter a data atual com LocalDate
        LocalDate dataAtual = LocalDate.now();
		// 2. Definir o formato para dia/mês/ano
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // 3. Formatar a data atual usando o formatador
        String dataFormatada = dataAtual.format(formatador);
		// DateTime dataVencimento = dataAtual.plusDays(10);
		return dataFormatada;
	}

}

