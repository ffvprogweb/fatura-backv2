package com.fatec.fatura.service;

public class ClienteServiceUnavailableException extends RuntimeException {
	public ClienteServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
