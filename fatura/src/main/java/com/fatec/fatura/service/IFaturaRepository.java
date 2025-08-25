package com.fatec.fatura.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fatec.fatura.model.Fatura;

@Repository
public interface IFaturaRepository extends JpaRepository<Fatura, Long> {
	/**
	 * consulta todas as faturas registradas para o cpf
	 * @param cpf
	 * @return array de faturas
	 */
    List<Fatura> findByCpf(String cpf);
    /**
     * consulta todas as faturas registradas para o cpf por data de emissao
     * @param cpf
     * @param dataEmissao
     * @return array de faturas
     */
	List<Fatura> findByCpfAndDataEmissao(String cpf, LocalDate dataEmissao);
}