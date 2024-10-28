package com.desafiowicket.pages;

import com.desafiowicket.model.ClienteForm;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;

public class NewClient extends BasePage{
    private static final long serialVersionUID = 1L;

    public NewClient() {
        add(new Label("titulo", "Cadastro de Cliente"));

        ClienteForm cliente = new ClienteForm();
        CompoundPropertyModel<ClienteForm> clienteFormCompoundPropertyModel = new CompoundPropertyModel<ClienteForm>(cliente);
        Form<ClienteForm> form = new Form<ClienteForm>("formCliente", clienteFormCompoundPropertyModel) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                ClienteForm cadastroCliente = getModelObject();
                System.out.println(cadastroCliente);

                setResponsePage(ClientList.class);
            }
        };
        add(form);

//        IModel<String> selecao = new Model<String>();
//        RadioGroup tipoPessoa = new RadioGroup("tipoPessoa", selecao);
//        tipoPessoa.add(new Radio("fisica", new Model<String>()));
//        tipoPessoa.add(new Radio("juridica", new Model<String>()));

        TextField<String> inputCpfCnpj = new TextField<String>("cpfCnpj");
        TextField<String> inputEmail = new TextField<String>("email");
        //TextField<Boolean> inputAtivo = new TextField<Boolean>("ativo");

        //pessoa física somente
        TextField<String> inputNome = new TextField<String>("nome");
        TextField<String> inputRg = new TextField<String>("rg");
        DateTextField inputDataNascimento = new DateTextField("dataNascimento");

        //pessoa jurídica somente
        TextField<String> inputRazaoSocial = new TextField<String>("razaoSocial");
        TextField<String> inputInscricaoEstadual = new TextField<String>("incricaoEstadual");
        DateTextField inputDataCriacao = new DateTextField("dataCriacao");

        form.add(inputCpfCnpj,
                inputEmail,
                inputNome,
                inputRg,
                inputDataNascimento,
                inputRazaoSocial,
                inputInscricaoEstadual,
                inputDataCriacao);
    }
}
