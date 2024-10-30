package com.desafiowicket.pages;

import com.desafiowicket.modal.EditClientModal;
import com.desafiowicket.model.ClienteForm;
import com.desafiowicket.service.HttpService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.markup.html.basic.Label;

import java.util.List;

public class ClientList extends BasePage {
    private static final long serialVersionUID = 502327979204314267L;
    private HttpService httpService = new HttpService();
    private ModalWindow modalEdit;

    public ClientList() {
        Label teste = new Label("teste", Model.of("Isso é um teste"));
        add(teste);

        modalEdit = new ModalWindow("modalEdit");
        modalEdit.setInitialWidth(600);
        modalEdit.setInitialHeight(455);
        modalEdit.setTitle("Editar Cliente");
        add(modalEdit);

        WebMarkupContainer clientListContainer = new WebMarkupContainer("clientListContainer");
        add(clientListContainer);

        try {
            List<ClienteForm> clientes = httpService.listaClientes();

            ListView<ClienteForm> listView = new ListView<ClienteForm>("clienteList", clientes) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void populateItem(ListItem<ClienteForm> item) {
                    final ClienteForm cliente = item.getModelObject();
                    item.add(new Label("nome", cliente.getNome()));
                    item.add(new Label("cpfCnpj", cliente.getCpfCnpj()));
                    item.add(new Label("rg", cliente.getRg()));
                    item.add(new Label("dataNascimento", cliente.getDataNascimento()));
                    item.add(new Label("email", cliente.getEmail()));

                    item.add(new AjaxLink<Void>("editButton") {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            modalEdit.setContent(new EditClientModal(modalEdit.getContentId(), new CompoundPropertyModel<>(cliente)) {
                                @Override
                                protected void onSave(AjaxRequestTarget target) {
                                    try {
                                        httpService.atualizaCliente(cliente);
                                        modalEdit.close(target);
                                        setResponsePage(ClientList.class);
                                    } catch (Exception e) {
                                        error("Erro ao atualizar cliente: " + e.getMessage());
                                    }
                                }
                            });
                            modalEdit.show(target);
                        }
                    });
                }
            };
            clientListContainer.add(listView);
        } catch (Exception e) {
            clientListContainer.setVisible(false);
            e.printStackTrace();
        }

    }
}
