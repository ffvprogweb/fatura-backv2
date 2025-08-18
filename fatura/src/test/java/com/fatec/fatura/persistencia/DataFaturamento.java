package com.fatec.fatura.persistencia;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DataFaturamento {
	public String obtemDataAtual() {
		DateTime data = new DateTime();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/YYYY");
		// DateTime dataVencimento = dataAtual.plusDays(10);
		return data.toString(fmt);
	}

}

