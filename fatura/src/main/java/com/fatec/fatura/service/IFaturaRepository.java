package com.fatec.fatura.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fatec.fatura.model.Fatura;

@Repository
public interface IFaturaRepository extends JpaRepository<Fatura, Long> {
	List<Fatura> findByCpfAndDataEmissao(String cpf, LocalDate dataEmissao);
}