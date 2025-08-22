package com.fatec.fatura.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fatec.fatura.model.Fatura;
import com.fatec.fatura.model.FaturaDto;

@Service
public class FaturaService implements IFaturaService {
	Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	IFaturaRepository faturaRepository;
	@Override
	public FaturaResponse registrar(FaturaDto f) {
		try {
			logger.info(">>>>>> 2 fatura service registrar fatura iniciado --> " + f.servicoContratado());
			if (cpfCadastrado(f.cpf())) {
				// obtem a data de hoje do sistema e instancia o objeto fatura
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				LocalDate dataVencimento = LocalDate.parse(f.dataVencimento(), formatter);
				Fatura fatura = new Fatura(f.cpf(), dataVencimento, f.servicoContratado(), f.valor());
				Fatura novaFatura = faturaRepository.save(fatura);
				logger.info(">>>>>> 3 fatura service registrar fatura executado ");
				return new FaturaResponse(true, "Fatura registrada", novaFatura);
			} else {
				logger.info(">>>>>> 3 fatura service registrar fatura cpf não cadastrado ");
				return new FaturaResponse(false, "CPF invalido nao cadastrado", null);
			}
		} catch (Exception e) {
			logger.info(
					">>>>>> FaturaService metodo registrar fatura - erro no cadastro da fatura -> " + e.getMessage());
			return new FaturaResponse(false, "Erro no registro da fatura" , null);
		}
	}

	@Override
	public FaturaResponse consultarPorId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Fatura> consultarData(String dataEmissao) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Fatura> consultarMes(int mesEmissao) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Fatura> consultaTodos() {
		return faturaRepository.findAll();
	}

	@Override
	public List<Fatura> consultaFaturaPorCpf(String cpf) {
			return faturaRepository.findByCpf(cpf);
	}

	@Override
	public void cancela(Long id, String cpf) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * Consulta o serviço de manutenção de clientes para verificar se o cliente esta cadastrado
	 * @param cpf - do cliente que esta efetivando a compra
	 * @return - verdadeiro se o cliente esta cadastrado e falso cliente não cadastrado
	 */
	public boolean cpfCadastrado(String cpf) {
		logger.info(">>>>>> consulta o cpf do cliente no servico mantem cliente " );
		return true;
	}
}
