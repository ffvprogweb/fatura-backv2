package com.fatec.fatura.service;

import com.fatec.fatura.model.Fatura;

public class FaturaResponse {
	private boolean sucesso;
	private String mensagem;
	private Fatura fatura;

	public FaturaResponse(boolean sucesso, String mensagem, Fatura fatura) {
		super();
		this.sucesso = sucesso;
		this.mensagem = mensagem;
		this.fatura = fatura;
	}

	public boolean isSucesso() {
		return sucesso;
	}

	public void setSucesso(boolean sucesso) {
		this.sucesso = sucesso;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Fatura getFatura() {
		return fatura;
	}

	public void setFatura(Fatura fatura) {
		this.fatura = fatura;
	}

}
