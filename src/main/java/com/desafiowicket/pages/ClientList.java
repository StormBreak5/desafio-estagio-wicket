package com.desafiowicket.pages;

import com.desafiowicket.icons.SvgIcon;
import com.desafiowicket.modal.DeleteConfirmationModal;
import com.desafiowicket.modal.EditClientModalPF;
import com.desafiowicket.model.ClienteForm;
import com.desafiowicket.model.TipoPessoa;
import com.desafiowicket.service.HttpService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.markup.html.basic.Label;

import java.util.List;
import java.util.stream.Collectors;

public class ClientList extends BasePage {
    private static final long serialVersionUID = 502327979204314267L;
    private HttpService httpService = new HttpService();
    private ModalWindow modalEdit;
    private FeedbackPanel feedback;

    public ClientList() {
        Label teste = new Label("teste", Model.of("Isso é um teste"));
        add(teste);

        feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        modalEdit = new ModalWindow("modalEdit");
        modalEdit.setInitialWidth(600);
        modalEdit.setInitialHeight(455);
        modalEdit.setTitle("");
        modalEdit.setCssClassName("custom-modal");
        add(modalEdit);

        WebMarkupContainer clientListPFContainer = new WebMarkupContainer("clientListPFContainer");
        clientListPFContainer.setOutputMarkupId(true);
        add(clientListPFContainer);

        WebMarkupContainer clientListPJContainer = new WebMarkupContainer("clientListPJContainer");
        clientListPJContainer.setOutputMarkupId(true);
        add(clientListPJContainer);

//        WebMarkupContainer clientListContainer = new WebMarkupContainer("clientListContainer");
//        clientListContainer.setOutputMarkupId(true);
//        add(clientListContainer);

        try {
            List<ClienteForm> clientes = httpService.listaClientes();

            List<ClienteForm> clientesPF = clientes.stream()
                    .filter(c -> TipoPessoa.FISICA.equals(c.getTipoPessoa()))
                    .collect(Collectors.toList());

            List<ClienteForm> clientesPJ = clientes.stream()
                    .filter(c -> TipoPessoa.JURIDICA.equals(c.getTipoPessoa()))
                    .collect(Collectors.toList());

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
                            modalEdit.setContent(new EditClientModalPF(modalEdit.getContentId(), new CompoundPropertyModel<>(cliente)) {
                                @Override
                                protected void onSave(AjaxRequestTarget target) {
                                    try {
                                        httpService.atualizaCliente(cliente);
                                        findParent(ModalWindow.class).close(target);
                                        target.add(findParent(ClientList.class).get("clientListContainer"));
                                        success("Cliente atualizado com sucesso!");
                                    } catch (Exception e) {
                                        error("Erro ao atualizar cliente: " + e.getMessage());
                                    }
                                }

                                @Override
                                protected void onCancel(AjaxRequestTarget target) {
                                    modalEdit.close(target);
                                }
                            });
                            modalEdit.show(target);
                        }
                    }.add(new SvgIcon("edit-icon", "icon-edit")));

                    item.add(new AjaxLink<Void>("deleteButton") {
                        @Override
                        protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                            super.updateAjaxAttributes(attributes);
                            AjaxCallListener ajaxCallListener = new AjaxCallListener();
                            ajaxCallListener.onPrecondition("return confirm('Tem certeza que deseja excluir este cliente?');");
                            attributes.getAjaxCallListeners().add(ajaxCallListener);
                        }

                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            try {
                                httpService.deletarClientePorId(cliente.getId());
                                success("Cliente deletado com sucesso!");
                                setResponsePage(ClientList.class);
                            } catch (Exception e) {
                                error("Erro ao deletar cliente: " + e.getMessage());
                                target.add(feedback);
                            }
                        }
                    }.add(new SvgIcon("delete-icon", "icon-delete")));
                }
            };
            clientListContainer.add(listView);
        } catch (Exception e) {
            clientListContainer.setVisible(false);
            e.printStackTrace();
        }

    }
}
