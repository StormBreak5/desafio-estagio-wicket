package com.desafiowicket.pages;

import com.desafiowicket.model.ClienteForm;
import com.desafiowicket.model.TipoPessoa;
import com.desafiowicket.service.HttpService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.PackageResourceReference;

import java.util.Arrays;

public class NewClient extends BasePage{
    private static final long serialVersionUID = 1L;
    FeedbackPanel fb;

    private HttpService clienteService = new HttpService();

    public NewClient() {
        add(new Label("titulo", "Cadastro de Cliente"));
        final ClienteForm cliente = new ClienteForm();
        fb = new FeedbackPanel("feedback");
        fb.setOutputMarkupPlaceholderTag(true);
        add(fb);

        //pessoa física somente
        final TextField<String> inputNome = new TextField<String>("nome");
        final TextField<String> inputCpf = new TextField<String>("cpf", new PropertyModel<String>(cliente, "cpfCnpj"));
        final TextField<String> inputRg = new TextField<String>("rg");
        final DateTextField inputDataNascimento = new DateTextField("dataNascimento", "yyyy-MM-dd");
        final TextField<String> inputEmailPF = new TextField<String>("emailPf", new PropertyModel<String>(cliente, "email"));

        //pessoa jurídica somente
        final TextField<String> inputRazaoSocial = new TextField<String>("razaoSocial");
        final TextField<String> inputCnpj = new TextField<String>("cnpj", new PropertyModel<String>(cliente, "CpfCnpj"));
        final TextField<String> inputInscricaoEstadual = new TextField<String>("inscricaoEstadual");
        final DateTextField inputDataCriacao = new DateTextField("dataCriacao", "yyyy-MM-dd");
        final TextField<String> inputEmailPJ = new TextField<String>("emailPj", new PropertyModel<String>(cliente, "email"));

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

        final WebMarkupContainer containerPF = new WebMarkupContainer("containerPF");
        final WebMarkupContainer containerPJ = new WebMarkupContainer("containerPJ");

        containerPF.setOutputMarkupPlaceholderTag(true);
        containerPJ.setOutputMarkupPlaceholderTag(true);

        containerPF.setVisible(false);
        containerPJ.setVisible(false);

        containerPF.add(inputNome, inputCpf, inputRg, inputDataNascimento, inputEmailPF);
        containerPJ.add(inputRazaoSocial, inputCnpj, inputInscricaoEstadual, inputDataCriacao, inputEmailPJ);

        comboTipoPessoa.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                TipoPessoa tipoPessoa = comboTipoPessoa.getModelObject();

                containerPF.setVisible(TipoPessoa.FISICA.equals(tipoPessoa));
                containerPJ.setVisible(TipoPessoa.JURIDICA.equals(tipoPessoa));

                target.add(containerPF);
                target.add(containerPJ);
            }
        });

        CompoundPropertyModel<ClienteForm> clienteFormCompoundPropertyModel = new CompoundPropertyModel<ClienteForm>(cliente);
        Form<ClienteForm> form = new Form<ClienteForm>("formCliente", clienteFormCompoundPropertyModel) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                add(fb);
                cliente.setTipoPessoa(comboTipoPessoa.getConvertedInput());
                if(comboTipoPessoa.getModelObject() == TipoPessoa.FISICA) {
                    cliente.setNome(inputNome.getValue());
                    cliente.setCpfCnpj(inputCpf.getValue());
                    cliente.setEmail(inputEmailPF.getValue());
                    cliente.setRg(inputRg.getValue());
                    cliente.setDataNascimento(inputDataNascimento.getConvertedInput());
                } else if(comboTipoPessoa.getModelObject() == TipoPessoa.JURIDICA) {
                    cliente.setRazaoSocial(inputRazaoSocial.getValue());
                    cliente.setDataCriacao(inputDataCriacao.getConvertedInput());
                    cliente.setInscricaoEstadual(inputInscricaoEstadual.getValue());
                    cliente.setCpfCnpj(inputCnpj.getValue());
                    cliente.setEmail(inputEmailPJ.getValue());
                }
                cliente.setAtivo(true);
                try {
                    clienteService.criarCliente(cliente);
                    System.out.println("Cadastro realizado com sucesso!");

                    setResponsePage(ClientList.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        form.add(comboTipoPessoa);
        form.add(containerPF);
        form.add(containerPJ);

        add(form);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(new PackageResourceReference(BasePage.class, "./NewClient.css")));
    }
}
