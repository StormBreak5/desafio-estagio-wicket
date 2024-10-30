package com.desafiowicket.pages;

import com.desafiowicket.model.ClienteForm;
import com.desafiowicket.service.HttpService;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.markup.html.basic.Label;

import java.util.List;

public class ClientList extends BasePage {
    private static final long serialVersionUID = 502327979204314267L;
    private HttpService httpService = new HttpService();

    public ClientList() throws Exception {
        Label teste = new Label("teste", Model.of("Isso é um teste"));
        add(teste);

        try {
            List<ClienteForm> clientes = httpService.listaClientes();

            ListView<ClienteForm> listView = new ListView<ClienteForm>("clienteList", clientes) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void populateItem(ListItem<ClienteForm> item) {
                    ClienteForm cliente = item.getModelObject();
                    item.add(new Label("nome", cliente.getNome()));
                    item.add(new Label("cpfCnpj", cliente.getCpfCnpj()));
                    item.add(new Label("rg", cliente.getRg()));
                    item.add(new Label("dataNascimento", cliente.getDataNascimento()));
                    item.add(new Label("email", cliente.getEmail()));
                }
            };
            add(listView);
        } catch (Exception e) {
            add(new Label("erro", Model.of("Erro ao carregar a lista de clientes" + e.getMessage()) ));
        }

    }
}
