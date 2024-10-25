package com.desafiowicket.page;

import com.desafiowicket.model.Cliente;
import com.desafiowicket.service.ClienteService;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ListClientesPage extends WebPage {

    @SpringBean
    private ClienteService clienteService;

    public ListClientesPage() {

        //Verifica se o service está nulo
        if(clienteService == null) {
            throw new IllegalStateException("ClienteService não foi injetado");
        }

        List<Cliente> cliente = clienteService.getAllClientes();

        try {
            ListView<Cliente> clienteListView = new ListView<Cliente>("clientes", cliente) {

                protected void populateItem(ListItem<Cliente> clienteListItem) {
                    Cliente cliente = clienteListItem.getModelObject();
                    clienteListItem.add(new Label("nome", cliente.getNome()));
                    clienteListItem.add(new Label("cpf", cliente.getCpfCnpj()));
                    clienteListItem.add(new Label("email", cliente.getEmail()));
                }
            };

            add(clienteListView);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao construir a listagem", e);
        }

    }
}
