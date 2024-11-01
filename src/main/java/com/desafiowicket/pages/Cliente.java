package com.desafiowicket.pages;

import com.desafiowicket.model.ClienteForm;
import com.desafiowicket.model.Endereco;
import com.desafiowicket.model.TipoPessoa;
import com.desafiowicket.service.HttpService;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;

public class Cliente extends BasePage{
    private static final long serialVersionUID = 1L;
    private HttpService httpService = new HttpService();
    FeedbackPanel feedback = new FeedbackPanel("feedback");

    public Cliente(Long clienteId) {
        feedback.setOutputMarkupId(true);
        add(feedback);

        try {
            ClienteForm cliente = httpService.buscaClientePorId(clienteId);

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

            ListView<Endereco> enderecosList = new ListView<Endereco>("enderecoList", cliente.getEnderecos()) {
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
                }
            };
            add(enderecosList);
        } catch (Exception e) {
            error("Erro ao carregar detalhes do cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
