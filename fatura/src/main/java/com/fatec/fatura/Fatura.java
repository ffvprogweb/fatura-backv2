package com.fatec.fatura;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Objects;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Fatura {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	String cpf;
	@JsonFormat(pattern = "dd/MM/yyyy")
	String dataEmissao;
	@JsonFormat(pattern = "dd/MM/yyyy")
	String dataVencimento;
	String servicoContratado;
	Double valor;
	boolean cancelada;
	boolean paga;

	public Fatura(String cpf, String dataVencimento, String servicoContratado, String valor) {

		setCpf(cpf);
		setDataEmissao();
		setDataVencimento(dataVencimento);
		setServicoContratado(servicoContratado);
		setValor(valor);
		setCancelada(false);
		setPaga(false);
	}

	public Fatura() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDataEmissao() {
		return dataEmissao;
	}

	public String getDataVencimento() {
		return dataVencimento;
	}

	public double getValor() {
		return valor;
	}

	public String setDataEmissao() {
		DateTime dataAtual = new DateTime();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/YYYY");
		this.dataEmissao = dataAtual.toString(fmt);
		return dataAtual.toString(fmt);
	}

	public String setDataVencimento(String data) {
		if ((data != null) && (dataIsValida(data)) && (dtVencMaiorDtAtual(getDataEmissao(), data))
				&& (!ehDomingo(data))) {
			this.dataVencimento = data;
			return data;
		} else {
			throw new IllegalArgumentException(
					"Data de vencimento: formato invalido ou domingo ou menor que data atual");
		}
	}

	/**
	 * Verifica se a data fornecida é um domingo.
	 *
	 * @param data A data no formato "dd/MM/yyyy".
	 * @return true se a data for um domingo e estiver em um formato válido, false
	 *         caso contrário.
	 */
	public boolean ehDomingo(String data) {
		if (dataIsValida(data) && data != null) {
			DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
			DateTime umaData = fmt.parseDateTime(data);
			return umaData.dayOfWeek().getAsText().equals("domingo");

		} else {
			return false;
		}
	}

	public boolean dataIsValida(String data) {
		if (data != null) {
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			df.setLenient(false);
			try {
				df.parse(data);
				return true;
			} catch (ParseException ex) {

				return false;
			}
		} else {
			return false;
		}
	}

	public boolean dtVencMaiorDtAtual(String dataAtual, String dataVencimento) {
		try {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
			DateTime dtAtual = formatter.parseDateTime(dataAtual);
			DateTime dtVenc = formatter.parseDateTime(dataVencimento);

			Days d = Days.daysBetween(dtAtual, dtVenc);
			return d.getDays() >= 0;
		} catch (Exception e) {

			throw new IllegalArgumentException("Fatura data invalida => " + e.getMessage());
		}
	}
	
	public void setValor(String valorString) {
		if (valorString == null || valorString.trim().isEmpty()) {
			throw new IllegalArgumentException("O valor da fatura não pode ser nulo ou vazio.");
		}

		try {
			double valorConvertido = Double.parseDouble(valorString);

			if (valorConvertido > 0) {
				this.valor = valorConvertido;
			} else {
				throw new IllegalArgumentException("O valor da fatura deve ser maior que zero.");
			}
		} catch (NumberFormatException e) {
			// Captura apenas a exceção de formato numérico inválido
			throw new IllegalArgumentException("Valor da fatura invalido", e);
		}
	}

	/*
	 * atribui o cnpj vefica se o cpf é valido
	 */
	public String setCpf(String cpf) {
		try {
			if (cpfIsValido(cpf)) {
				this.cpf = cpf;
				return cpf;
			} else {
				throw new IllegalArgumentException("CPF invalido");
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("CPF invalido");
		}

	}

	public String getCpf() {
		return this.cpf;
	}

	public String getServicoContratado() {
		return servicoContratado;
	}

	public String setServicoContratado(String servico) {

		if ((servico == null) || (servico.isBlank())) {
			throw new IllegalArgumentException("Descricao do servico invalido");
		} else {
			this.servicoContratado = servico;
			return servico;
		}

	}
	

	public boolean isCancelada() {
		return cancelada;
	}

	public void setCancelada(boolean cancelada) {
		this.cancelada = cancelada;
	}

	public static boolean cpfIsValido(String cpf) {
		if (cpf.equals("00000000000") || cpf.equals("11111111111") || cpf.equals("22222222222")
				|| cpf.equals("33333333333") || cpf.equals("44444444444") || cpf.equals("55555555555")
				|| cpf.equals("66666666666") || cpf.equals("77777777777") || cpf.equals("88888888888")
				|| cpf.equals("99999999999") || (cpf.length() != 11))
			return (false);

		char dig10, dig11;
		int sm, i, r, num, peso;

		try {
			sm = 0;
			peso = 10;
			for (i = 0; i < 9; i++) {
				num = (cpf.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);

			if ((r == 10) || (r == 11)) {
				dig10 = '0';
			} else {
				dig10 = (char) (r + 48);
			}

			sm = 0;
			peso = 11;

			for (i = 0; i < 10; i++) {
				num = (cpf.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);

			if ((r == 10) || (r == 11)) {
				dig11 = '0';
			} else {
				dig11 = (char) (r + 48);
			}

			if ((dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10))) {
				return (true);
			} else {
				return (false);
			}
		} catch (InputMismatchException erro) {
			return (false);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataEmissao, dataVencimento, valor);
	}

	@Override
	public String toString() {
		return "Fatura [id=" + id + ", cpf=" + cpf + ", dataEmissao=" + dataEmissao + ", dataVencimento="
				+ dataVencimento + ", servicoContratado=" + servicoContratado + ", valor=" + valor + "]";
	}

	public boolean isPaga() {
		return paga;
	}

	public void setPaga(boolean paga) {
		this.paga = paga;
	}

	
}