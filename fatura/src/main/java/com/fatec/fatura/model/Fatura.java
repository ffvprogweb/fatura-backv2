package com.fatec.fatura.model;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Fatura {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 11)
	private String cpf;

	@Column(nullable = false)
	private LocalDate dataEmissao;

	@Column(nullable = false)
	private LocalDate dataVencimento;

	@Column(nullable = false, length = 100)
	private String servicoContratado;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal valor;

	@Column(nullable = false)
	private boolean cancelada;

	@Column(nullable = false)
	private boolean paga;

	// Construtores
	public Fatura() {
	}

	public Fatura(String cpf, LocalDate dataVencimento, String servicoContratado, String valor) {
		setCpf(cpf);
		setDataVencimento(dataVencimento);
		setServicoContratado(servicoContratado);
		setValor(valor);
		this.dataEmissao = LocalDate.now();
		setCancelada(false);
		setPaga(false);
	}

	// Getters e Setters
	public Long getId() {
		return id;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		try {
			if (isValido(cpf)) {
				this.cpf = cpf;

			} else {
				throw new IllegalArgumentException("CPF invalido");
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("CPF invalido");
		}

	}

	public LocalDate getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(LocalDate dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate dataVencimento) {
		if (dataVencimento != null && (!isDomingo(dataVencimento)) && (!isAnteriorDataAtual(dataVencimento))) {
			this.dataVencimento = dataVencimento;
			System.out.println(">>>>>> data vencimento => " + dataVencimento);

		} else {
			throw new IllegalArgumentException(
					"Data de vencimento: formato invalido ou domingo ou menor que data atual");
		}
	}

	public String getServicoContratado() {
		return servicoContratado;
	}

	public void setServicoContratado(String servico) {
		System.out.println("servico ==> " + servico);
		if (servico.isEmpty()) {
			throw new IllegalArgumentException("Descricao do servico invalido");
		} else {
			this.servicoContratado = servico;
		}
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(String v) {
      
		try {
			BigDecimal temp = new BigDecimal(v);
			
			//BigDecimal val = new BigDecimal(v.replace(",", "."));
			this.valor = temp;
			if (valor.compareTo(BigDecimal.ZERO) <= 0) {
				throw new IllegalArgumentException("O valor da fatura deve ser maior que zero.");
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Valor da fatura invalido");
		}
	}

	public boolean isCancelada() {
		return cancelada;
	}

	public void setCancelada(boolean c) {
		this.cancelada = c;
	}

	public boolean isPaga() {
		return paga;
	}

	public void setPaga(boolean p) {
		this.paga = p;
	}

	public boolean isDomingo(LocalDate date) {

		if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isAnteriorDataAtual(LocalDate date) {
		if (date.isBefore(LocalDate.now())) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isValido(String cpf) {
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

}