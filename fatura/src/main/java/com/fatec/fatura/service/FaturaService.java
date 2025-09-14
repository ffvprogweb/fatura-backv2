package com.fatec.fatura.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fatec.fatura.model.ClienteDto;
import com.fatec.fatura.model.Fatura;
import com.fatec.fatura.model.FaturaDto;

@Service
public class FaturaService implements IFaturaService {
	Logger logger = LogManager.getLogger(this.getClass());
	IFaturaRepository faturaRepository;
	
	private RestTemplate restTemplate;
	@Value("${api.cliente.url}")
	private String apiClienteUrl;

	/*
	 * injecao de dependencia pelo construtor nao usa a construção implicita @Autowired
	 */
	public FaturaService(IFaturaRepository faturaRepository, RestTemplate restTemplate) {
		this.faturaRepository = faturaRepository;
		this.restTemplate = restTemplate;
	}
	@Override
	/**
	 * Objetivo - registrar fatura no db - ajusta automaticamente datas invalidas que excedem o numero de dias de um mes
	 */
	public FaturaResponse registrar(FaturaDto f) {
		Fatura novaFatura = null;
		LocalDate dataVencimento = null;
		try {
			logger.info(">>>>>> 2 fatura service metodo registrar fatura iniciado");

			// Verifica se o CPF está cadastrado primeiro
			if (!cpfCadastrado(f.cpf())) {
				logger.info(">>>>>> 3 fatura service registrar fatura cpf não cadastrado ");
				return new FaturaResponse(false, "CPF invalido nao cadastrado", null);
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");// confg padrao (smart) nao lanca erro ajusta a data automaticamente
			dataVencimento = LocalDate.parse(f.dataVencimento(), formatter); 
			logger.info(">>>>>> 3 fatura service registrar parse executado ==> " + dataVencimento.toString());
			// Se a data for válida (o parse não lançou exceção), a execução continua.
			Fatura temp = new Fatura(f.cpf(), dataVencimento, f.servicoContratado(), f.valor());
			novaFatura = faturaRepository.save(temp);
			logger.info(">>>>>> 3 fatura service registrar fatura save executado ");
			return new FaturaResponse(true, "Fatura registrada", novaFatura);

		} catch (DateTimeParseException e) {
	        logger.warn(">>>>>> Data inválida: {}", e.getMessage());
	        return new FaturaResponse(false, "Data de vencimento inválida. Verifique a data fornecida.", null);

	    } catch (ClienteServiceUnavailableException e) {
	        logger.error(">>>>>> Falha ao consultar serviço de clientes: {}", e.getMessage());
	        return new FaturaResponse(false, "Não foi possível validar o CPF. Serviço de clientes indisponível.", null);

	    } catch (Exception e) {
	        logger.error(">>>>>> Erro inesperado no registro de fatura: {}", e.getMessage(), e);
	        return new FaturaResponse(false, "Erro inesperado no registro da fatura", null);
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
	 * Consulta o serviço de manutenção de clientes para verificar se o cliente esta
	 * cadastrado
	 * 
	 * @param cpf - do cliente que esta efetivando a compra
	 * @return - verdadeiro se o cliente esta cadastrado e falso cliente não
	 *         cadastrado
	 */
	public boolean cpfCadastrado(String cpf) {
	    logger.info(">>>>>> metodo cpfCadastrado consultando CPF: {}", cpf);

	    ClienteDto clienteRequest = new ClienteDto(cpf, "", "", "");
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<ClienteDto> requestEntity = new HttpEntity<>(clienteRequest, headers);

	    try {
	        ResponseEntity<ClienteDto> response =
	                restTemplate.postForEntity(apiClienteUrl, requestEntity, ClienteDto.class);

	        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
	            logger.info(">>>>>> CPF {} encontrado. Detalhes: {}", cpf, response.getBody());
	            return true;
	        }
	        logger.warn(">>>>>> CPF {} não encontrado. Status: {}", cpf, response.getStatusCode());
	        return false;

	    } catch (HttpClientErrorException e) {
	    	int statusCode = e.getStatusCode().value(); // obtem o codigo de status
	        if (statusCode == 404 || statusCode == 400) {
	            // 404 ou 400 -> CPF não cadastrado
	            logger.info(">>>>>> CPF {} não cadastrado (status {})", cpf, statusCode);
	            return false;
	        }
	        // Qualquer outro erro 4xx/5xx -> serviço indisponível
	        logger.error("Erro na chamada da API de clientes: {}", e.getMessage(), e);
	        throw new ClienteServiceUnavailableException("Serviço de clientes indisponível");
	    } catch (ResourceAccessException e) {
	        // Timeout ou problema de rede
	        logger.error("Erro de comunicação com o serviço de clientes: {}", e.getMessage());
	        throw new ClienteServiceUnavailableException("Serviço de clientes indisponível");
	    }
	}


}
