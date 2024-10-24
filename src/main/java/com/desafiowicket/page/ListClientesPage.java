package com.desafiowicket.page;

import com.desafiowicket.model.Cliente;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListClientesPage extends WebPage {

    public ListClientesPage() {
        List<Cliente> cliente = getClientesFromAPI();

        ListView<Cliente> clienteListView = new ListView<Cliente>("clientes", cliente) {

            protected void populateItem(ListItem<Cliente> clienteListItem) {
                Cliente cliente = clienteListItem.getModelObject();
                clienteListItem.add(new Label("nome", cliente.getNome()));
                clienteListItem.add(new Label("cpf", cliente.getCpfCnpj()));
                clienteListItem.add(new Label("email", cliente.getEmail()));
            }
        };

        add(clienteListView);
    }

    private List<Cliente> getClientesFromAPI() {
        List<Cliente> clientes = new ArrayList<>();

        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet("https://localhost:8080/api/clientes");
            HttpResponse response = httpClient.execute(request);

            if(response.getStatusLine().getStatusCode() == 200) {
                String json = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
