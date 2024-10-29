package com.saproject.bancosa.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ViaCepService {

    private final String VIACEP_URL = "https://viacep.com.br/ws/%s/json/";

    public Endereco buscarEnderecoPorCep(String cep) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(VIACEP_URL, cep);
        return restTemplate.getForObject(url, Endereco.class);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Endereco {
        private String logradouro;
        private String bairro;
        private String localidade;
        private String uf;

        public String getLogradouro() {
            return logradouro;
        }

        public void setLogradouro(String logradouro) {
            this.logradouro = logradouro;
        }

        public String getBairro() {
            return bairro;
        }

        public void setBairro(String bairro) {
            this.bairro = bairro;
        }

        public String getLocalidade() {
            return localidade;
        }

        public void setLocalidade(String localidade) {
            this.localidade = localidade;
        }

        public String getUf() {
            return uf;
        }

        public void setUf(String uf) {
            this.uf = uf;
        }

        @Override
        public String toString() {
            return logradouro + ", " + bairro + ", " + localidade + " - " + uf;
        }
    }
}