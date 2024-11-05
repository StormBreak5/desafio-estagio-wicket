package com.desafiowicket.pages;

import com.desafiowicket.icons.SvgIcon;
import com.desafiowicket.modal.NewAddressModal;
import com.desafiowicket.model.ClienteForm;
import com.desafiowicket.model.Endereco;
import com.desafiowicket.model.TipoPessoa;
import com.desafiowicket.service.HttpService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;

public class Cliente extends BasePage{
    private static final long serialVersionUID = 1L;
    private HttpService httpService = new HttpService();
    private ModalWindow modalNovoEndereco;
    private ClienteForm cliente;
    private ListView<Endereco> enderecosList;
    private FeedbackPanel feedback;
    private WebMarkupContainer enderecosContainer;

    public Cliente(Long clienteId) {
        feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        enderecosContainer = new WebMarkupContainer("enderecosContainer");
        enderecosContainer.setOutputMarkupId(true);
        add(enderecosContainer);

        modalNovoEndereco = new ModalWindow("modalNovoEndereco");
        modalNovoEndereco.setInitialHeight(700);
        modalNovoEndereco.setInitialWidth(800);
        modalNovoEndereco.setTitle("");
        modalNovoEndereco.setCssClassName("custom-modal");
        add(modalNovoEndereco);

        try {
            cliente = httpService.buscaClientePorId(clienteId);

            WebMarkupContainer pfContainer = new WebMarkupContainer("pfContainer");
            WebMarkupContainer pjContainer = new WebMarkupContainer("pjContainer");

            if(TipoPessoa.FISICA.equals(cliente.getTipoPessoa())){
                pjContainer.setVisible(false);
                pfContainer.add(new Label("nome", Model.of(cliente.getNome())));
                pfContainer.add(new Label("cpf", Model.of(cliente.getCpfCnpj())));
                pfContainer.add(new Label("rg", Model.of(cliente.getRg())));
                pfContainer.add(new Label("dataNascimento", Model.of(cliente.getDataNascimento())));
            } else {
                pfContainer.setVisible(false);
                pjContainer.add(new Label("razaoSocial", Model.of(cliente.getRazaoSocial())));
                pjContainer.add(new Label("cnpj", Model.of(cliente.getCpfCnpj())));
                pjContainer.add(new Label("inscricaoEstadual", Model.of(cliente.getInscricaoEstadual())));
                pjContainer.add(new Label("dataCriacao", Model.of(cliente.getDataCriacao())));
            }

            add(pfContainer);
            add(pjContainer);

            add(new Label("email", Model.of(cliente.getEmail())));
            add(new Label("status", Model.of(cliente.getAtivo() ? "Ativo" : "Inativo")));

            enderecosList = new ListView<Endereco>("enderecoList", cliente.getEnderecos()) {
                @Override
                protected void populateItem(ListItem<Endereco> item) {
                    Endereco endereco = item.getModelObject();
                    item.add(new Label("logradouro", Model.of(endereco.getLogradouro())));
                    item.add(new Label("numero", Model.of(endereco.getNumero())));
                    item.add(new Label("complemento", Model.of(endereco.getComplemento())));
                    item.add(new Label("bairro", Model.of(endereco.getBairro())));
                    item.add(new Label("cidade", Model.of(endereco.getCidade())));
                    item.add(new Label("uf", Model.of(endereco.getUf())));
                    item.add(new Label("cep", Model.of(endereco.getCep())));
                    item.add(new Label("telefone", Model.of(endereco.getTelefone())));
                    item.add(new Label("principal", Model.of(endereco.getEnderecoPrincipal() ? "Sim" : "Não")));

//                    item.add(new AjaxLink<Void>("editButton") {
//                        @Override
//                        public void onClick(AjaxRequestTarget ajaxRequestTarget) {
//
//                        }
//                    }.add(new SvgIcon("edit-icon", "icon-edit")));
//
//                    item.add(new AjaxLink<Void>("deleteButton") {
//                        @Override
//                        protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
//                            super.updateAjaxAttributes(attributes);
//                        }
//
//                        @Override
//                        public void onClick(AjaxRequestTarget ajaxRequestTarget) {
//
//                        }
//                    }.add(new SvgIcon("delete-icon", "icon-delete")));
                }
            };
            enderecosContainer.add(enderecosList);

            add(new AjaxLink<Void>("botaoNovoEndereco") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    showNewAddressModal(target);
                }
            });
        } catch (Exception e) {
            error("Erro ao carregar detalhes do cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showNewAddressModal(AjaxRequestTarget target) {
        Endereco novoEndereco = new Endereco();
        CompoundPropertyModel<Endereco> enderecoModel = new CompoundPropertyModel<>(novoEndereco);

        modalNovoEndereco.setContent(new NewAddressModal(modalNovoEndereco.getContentId(), enderecoModel) {
            @Override
            protected void onSave(AjaxRequestTarget target) {
                try {
                    if (Boolean.TRUE.equals(novoEndereco.getEnderecoPrincipal())) {
                        for (Endereco endereco : cliente.getEnderecos()) {
                            endereco.setEnderecoPrincipal(false);
                        }
                    } else if (cliente.getEnderecos().isEmpty()) {
                        novoEndereco.setEnderecoPrincipal(true);
                    }

                    cliente.getEnderecos().add(novoEndereco);
                    httpService.atualizaCliente(cliente);

                    enderecosList.setModelObject(cliente.getEnderecos());

                    Cliente.this.success("Endereço adicionado com sucesso!");

                    target.add(enderecosContainer);
                    target.add(Cliente.this.feedback);

                    modalNovoEndereco.close(target);
                } catch (Exception e) {
                    error("Erro ao salvar endereço: " + e.getMessage());
                    target.add(Cliente.this.feedback);
                }
            }

            @Override
            protected void onCancel(AjaxRequestTarget target) {
                modalNovoEndereco.close(target);
            }
        });

        modalNovoEndereco.show(target);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(new PackageResourceReference(Cliente.class, "./Cliente.css")));
    }
}
