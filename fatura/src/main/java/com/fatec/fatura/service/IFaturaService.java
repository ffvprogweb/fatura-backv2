package com.fatec.fatura.service;

import java.util.List;

import com.fatec.fatura.model.Fatura;
import com.fatec.fatura.model.FaturaDto;

public interface IFaturaService {
	// Req16 - prioridade alta
	public FaturaResponse registrar(FaturaDto fatura);

	// Req17 - prioridade alta
	public FaturaResponse consultarPorId(String id);

	public List<Fatura> consultarData(String dataEmissao);

	public List<Fatura> consultarMes(int mesEmissao);

	public List<Fatura> consultaTodos();

//Req18 - prioridade media
	public List<Fatura> consultaCpf(String cpf);

//Req19 - prioridade media
	public void cancela(Long id, String cpf);
}
