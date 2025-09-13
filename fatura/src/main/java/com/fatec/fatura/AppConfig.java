package com.fatec.fatura;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
	/*
	 * conexao sincrona com o servico de cliente - se a API não responder
	 * a aplicação não fica bloqueada
	 */
    @Bean
    public RestTemplate restTemplate() {
    	SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5s
        factory.setReadTimeout(5000);    // 5s
        return new RestTemplate(factory);
    }
}