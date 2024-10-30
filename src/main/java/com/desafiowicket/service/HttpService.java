package com.desafiowicket.service;

import com.desafiowicket.model.ClienteForm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpService implements Serializable {

    private final String apiUrl = "http://localhost:8081/";
    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public List<ClienteForm> listaClientes() throws Exception {
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(apiUrl + "clientes");

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                int codStatus = response.getStatusLine().getStatusCode();
                if (codStatus == 200) {
                    try (InputStream content = response.getEntity().getContent()) {
                        String jsonString = IOUtils.toString(content, StandardCharsets.UTF_8);
                        PaginatedResponseService<ClienteForm> paginatedResponse = mapper
                                .readValue(jsonString, new TypeReference<PaginatedResponseService<ClienteForm>>() {});

                        return paginatedResponse.getContent();
                    }
                } else {
                    throw new RuntimeException("Erro ao obter os clientes: " + codStatus);
                }
            }
        }
    }

    public void criarCliente(ClienteForm cliente) throws Exception {
        URL url = new URL(apiUrl + "clientes");
        String json = mapper.writeValueAsString(cliente);

        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url.toString());
            httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int codStatus = response.getStatusLine().getStatusCode();
                if(codStatus == 200) {
                    System.out.println("Cliente cadastrado com sucesso!");
                } else {
                    System.out.println("Erro ao cadastrar cliente: " + codStatus);
                }
            }
        }
    };

    public void atualizaCliente(ClienteForm cliente) throws Exception {
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String json = mapper.writeValueAsString(cliente);
            HttpPut httpPut = new HttpPut(apiUrl + "clientes/" + cliente.getId());

            try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
                int codStatus = response.getStatusLine().getStatusCode();
                if(codStatus == 200) {
                    System.out.println("Cliente atualizado com sucesso!");
                } else {
                    System.out.println("Erro ao atualizar cliente: " + codStatus);
                }
            }
        }
    }

    public void deletarClientePorId(Long id) throws Exception {
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete httpDelete = new HttpDelete(apiUrl + "clientes/" + id);

            try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {
                int codStatus = response.getStatusLine().getStatusCode();
                if(codStatus == 200 || codStatus == 204) {
                    System.out.println("Cliente deletado com sucesso!");
                } else {
                    System.out.println("Erro ao deletar cliente: " + codStatus);
                }
            }
        }
    }
}
