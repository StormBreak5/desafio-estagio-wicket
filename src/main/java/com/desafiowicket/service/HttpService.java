package com.desafiowicket.service;

import com.desafiowicket.model.ClienteForm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

public class HttpService implements Serializable {

    private final String apiUrl = "http://localhost:8081/";
    ObjectMapper mapper = new ObjectMapper();

    public List<ClienteForm> listaClientes() throws Exception {
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(apiUrl + "clientes");

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                int codStatus = response.getStatusLine().getStatusCode();
                if (codStatus == 200) {
                    try (InputStream content = response.getEntity().getContent()) {
                        return mapper.readValue(content, new TypeReference<List<ClienteForm>>() {});
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
}
