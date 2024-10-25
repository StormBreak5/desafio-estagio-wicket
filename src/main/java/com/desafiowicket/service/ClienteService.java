package com.desafiowicket.service;

import com.desafiowicket.model.Cliente;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClienteService {
    private static final String API_URL = "http://localhost:8081/clientes";

    public List<Cliente> getAllClientes() {
        List<Cliente> clientes = new ArrayList<>();

        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(API_URL);
            HttpResponse response = httpClient.execute(request);

            if(response.getStatusLine().getStatusCode() == 200) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                JSONArray jsonArray = new JSONArray(jsonResponse);

                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Cliente cliente = new Cliente();
                    cliente.setNome(obj.getString("nome"));
                    cliente.setCpfCnpj(obj.getString("cpfCnpj"));
                    cliente.setEmail(obj.getString("email"));
                    clientes.add(cliente);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return clientes;
    }
}
