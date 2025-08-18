package com.fatec.fatura.persistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

	public static List<FaturaMassaDeTeste> lerArquivo(String caminhoArquivo) throws IOException {
		List<FaturaMassaDeTeste> faturas = new ArrayList<>();
		String linha;

		try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
			// Ler a primeira linha (cabeçalho) e ignorar, se houver
			br.readLine();

			while ((linha = br.readLine()) != null) {
				 // Ignorar linhas em branco ou vazias
                if (linha.trim().isEmpty()) {
                    continue; 
                }
				// Dividir a linha por vírgula
				String[] campos = linha.split(",");

				// Verificar se a linha tem o número correto de campos
				if (campos.length == 5) {
					try {
						String cpf = campos[0].trim();
						String dataVencimento = campos[1].trim();
						String desc = campos[2].trim();
						String valor = campos[3].trim();
						String re = campos[4].trim();

						// Criar um objeto Fatura com os dados lidos
						FaturaMassaDeTeste fatura = new FaturaMassaDeTeste(cpf, dataVencimento, desc, valor, re);
						faturas.add(fatura);
					} catch (Exception e) {
						System.err.println("Erro na leitura do arquivo na linha: " + linha);

					}
				} else {
					System.err.println("Linha inválida no arquivo CSV (número incorreto de campos): " + linha);

				}
			}
		}
		return faturas;
	}
}