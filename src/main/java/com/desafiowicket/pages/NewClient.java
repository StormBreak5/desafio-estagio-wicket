package com.desafiowicket.pages;

import com.desafiowicket.model.ClienteForm;
import com.desafiowicket.model.TipoPessoa;
import com.desafiowicket.service.HttpService;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.model.CompoundPropertyModel;

import java.util.Arrays;

public class NewClient extends BasePage{
    private static final long serialVersionUID = 1L;

    private HttpService clienteService = new HttpService();

    public NewClient() {
        add(new Label("titulo", "Cadastro de Cliente"));

        final TextField<String> inputCpfCnpj = new TextField<String>("cpfCnpj");
        final TextField<String> inputEmail = new TextField<String>("email");

        //pessoa física somente
        final TextField<String> inputNome = new TextField<String>("nome");
        final TextField<String> inputRg = new TextField<String>("rg");
        final DateTextField inputDataNascimento = new DateTextField("dataNascimento", "yyyy-MM-dd");

        //pessoa jurídica somente
        final TextField<String> inputRazaoSocial = new TextField<String>("razaoSocial");
        final TextField<String> inputInscricaoEstadual = new TextField<String>("incricaoEstadual");
        final DateTextField inputDataCriacao = new DateTextField("dataCriacao");

        final DropDownChoice<TipoPessoa> comboTipoPessoa = new DropDownChoice<>("tipoPessoa",
                Arrays.asList(TipoPessoa.values()),
                new IChoiceRenderer<TipoPessoa>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Object getDisplayValue(TipoPessoa tipoPessoa) {
                        return tipoPessoa.getLabel();
                    }

                    @Override
                    public String getIdValue(TipoPessoa object, int index) {
                        return object.name();
                    }

                    @Override
                    public void detach() {}
                });

        final ClienteForm cliente = new ClienteForm();
        CompoundPropertyModel<ClienteForm> clienteFormCompoundPropertyModel = new CompoundPropertyModel<ClienteForm>(cliente);
        Form<ClienteForm> form = new Form<ClienteForm>("formCliente", clienteFormCompoundPropertyModel) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                cliente.setNome(inputNome.getValue());
                cliente.setCpfCnpj(inputCpfCnpj.getValue());
                cliente.setEmail(inputEmail.getValue());
                cliente.setRg(inputRg.getValue());
                cliente.setDataNascimento(inputDataNascimento.getConvertedInput());
                cliente.setRazaoSocial(inputRazaoSocial.getValue());
                cliente.setDataCriacao(inputDataCriacao.getModelObject());
                cliente.setIncricaoEstadual(inputInscricaoEstadual.getValue());
                cliente.setTipoPessoa(comboTipoPessoa.getConvertedInput());
                cliente.setAtivo(true);
                try {
                    clienteService.criarCliente(cliente);
                    System.out.println("Cadastro realizado com sucesso!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        add(form);

        form.add(inputCpfCnpj,
                inputEmail,
                inputNome,
                inputRg,
                inputDataNascimento,
                inputRazaoSocial,
                inputInscricaoEstadual,
                inputDataCriacao,
                comboTipoPessoa);
    }
}
