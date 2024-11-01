package com.desafiowicket.pages;

import com.desafiowicket.icons.SvgIcon;
import com.desafiowicket.modal.EditClientModalPF;
import com.desafiowicket.modal.EditClientModalPJ;
import com.desafiowicket.model.ClienteForm;
import com.desafiowicket.model.TipoPessoa;
import com.desafiowicket.service.HttpService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.resource.PackageResourceReference;

import java.util.List;
import java.util.stream.Collectors;

public class ClientList extends BasePage {
    private static final long serialVersionUID = 502327979204314267L;
    private HttpService httpService = new HttpService();
    private ModalWindow modalEdit;
    private FeedbackPanel feedback;

    private WebMarkupContainer clientListPFContainer;
    private WebMarkupContainer clientListPJContainer;

    public ClientList() {

        feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        modalEdit = new ModalWindow("modalEdit");
        modalEdit.setInitialWidth(600);
        modalEdit.setInitialHeight(455);
        modalEdit.setTitle("");
        modalEdit.setCssClassName("custom-modal");
        add(modalEdit);

        clientListPFContainer = new WebMarkupContainer("clientListPFContainer");
        clientListPFContainer.setOutputMarkupId(true);
        add(clientListPFContainer);

        clientListPJContainer = new WebMarkupContainer("clientListPJContainer");
        clientListPJContainer.setOutputMarkupId(true);
        add(clientListPJContainer);

        try {
            List<ClienteForm> clientes = httpService.listaClientes();

            List<ClienteForm> clientesPF = clientes.stream()
                    .filter(c -> TipoPessoa.FISICA.equals(c.getTipoPessoa()))
                    .collect(Collectors.toList());

            List<ClienteForm> clientesPJ = clientes.stream()
                    .filter(c -> TipoPessoa.JURIDICA.equals(c.getTipoPessoa()))
                    .collect(Collectors.toList());

            ListView<ClienteForm> listViewPF = new ListView<ClienteForm>("listViewPF", clientesPF) {

                @Override
                protected void populateItem(ListItem<ClienteForm> item) {
                    final ClienteForm clientePF = item.getModelObject();

                    AjaxLink<Void> nameLink = new AjaxLink<Void>("nameLink") {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            setResponsePage(new Cliente(clientePF.getId()));
                        }
                    };
                    nameLink.add(new Label("nome", clientePF.getNome()));

                    item.add(nameLink);
                    item.add(new Label("cpf", clientePF.getCpfCnpj()));
                    item.add(new Label("rg", clientePF.getRg()));
                    item.add(new Label("dataNascimento", clientePF.getDataNascimento()));
                    item.add(new Label("email", clientePF.getEmail()));
                    addActionBtns(item, clientePF);
                }
            };

            ListView<ClienteForm> listViewPJ = new ListView<ClienteForm>("listViewPJ", clientesPJ) {
                @Override
                protected void populateItem(ListItem<ClienteForm> item) {
                    final ClienteForm clientePJ = item.getModelObject();

                    AjaxLink<Void> nameLink = new AjaxLink<Void>("nameLink") {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            setResponsePage(new Cliente(clientePJ.getId()));
                        }
                    };
                    nameLink.add(new Label("razaoSocial", clientePJ.getRazaoSocial()));

                    item.add(nameLink);
                    item.add(new Label("cnpj", clientePJ.getCpfCnpj()));
                    item.add(new Label("inscricaoEstadual", clientePJ.getInscricaoEstadual()));
                    item.add(new Label("dataCriacao", clientePJ.getDataCriacao()));
                    item.add(new Label("email", clientePJ.getEmail()));
                    addActionBtns(item, clientePJ);
                }
            };

            clientListPFContainer.add(listViewPF);
            clientListPJContainer.add(listViewPJ);
        } catch (Exception e) {
            error("Erro ao carregar lista de clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addActionBtns(ListItem<ClienteForm> item, ClienteForm cliente) {
        item.add(new AjaxLink<Void>("editButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                showEditModal(target, cliente);
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
                    target.add(findParent(ClientList.class).get("clientListPFContainer"));
                    target.add(findParent(ClientList.class).get("clientListPJContainer"));
                    target.add(feedback);
                } catch (Exception e) {
                    error("Erro ao deletar cliente: " + e.getMessage());
                    target.add(feedback);
                }
            }
        }.add(new SvgIcon("delete-icon", "icon-delete")));
    }

    private void showEditModal(AjaxRequestTarget target, ClienteForm cliente) {
        if(TipoPessoa.FISICA.equals(cliente.getTipoPessoa())) {
            modalEdit.setContent(new EditClientModalPF(modalEdit.getContentId(), new CompoundPropertyModel<>(cliente)) {
                @Override
                protected void onSave(AjaxRequestTarget target) {
                    salvarCliente(target, cliente);
                }

                @Override
                protected void onCancel(AjaxRequestTarget target) {
                    modalEdit.close(target);
                }
            });
        } else {
            modalEdit.setContent(new EditClientModalPJ(modalEdit.getContentId(), new CompoundPropertyModel<>(cliente)) {
                @Override
                protected void onSave(AjaxRequestTarget target) {
                    salvarCliente(target, cliente);
                }

                @Override
                protected void onCancel(AjaxRequestTarget target) {
                    modalEdit.close(target);
                }
            });
        }
        modalEdit.show(target);
    }

    private void salvarCliente(AjaxRequestTarget target, ClienteForm cliente) {
        try {
            httpService.atualizaCliente(cliente);
            updateListaClientes(target);
            success("Cliente atualizado com sucesso!");
            modalEdit.close(target);
        } catch (Exception e) {
            error("Erro ao atualizar cliente: " + e.getMessage());
        }
        target.add(feedback);
    }

    private void updateListaClientes(AjaxRequestTarget target) {
        target.add(clientListPFContainer);
        target.add(clientListPJContainer);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(new PackageResourceReference(BasePage.class, "./ClientList.css")));
    }
}
