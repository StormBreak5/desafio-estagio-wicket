package com.desafiowicket.modal;

import com.desafiowicket.model.Endereco;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;

public abstract class NewAddressModal extends Panel {
    private FeedbackPanel feedback;

    public interface SaveAddressCallback {
        void onSave(Endereco endereco);
    }

    private SaveAddressCallback callback;

    public NewAddressModal(String id, IModel<Endereco> model) {
        super(id, model);

        feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        Form<Endereco> form = new Form<>("form", model);
        form.setOutputMarkupId(true);
        add(form);

        form.add(new TextField<>("logradouro").setRequired(true));
        form.add(new TextField<>("numero").setRequired(true));
        form.add(new TextField<>("complemento"));
        form.add(new TextField<>("cep").setRequired(true));
        form.add(new TextField<>("bairro").setRequired(true));
        form.add(new TextField<>("cidade").setRequired(true));
        form.add(new TextField<>("uf").setRequired(true));
        form.add(new TextField<>("telefone"));

        CheckBox enderecoPrincipal = new CheckBox("enderecoPrincipal");
        form.add(enderecoPrincipal);

        form.add(new AjaxButton("submitButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                try {
                    onSave(target);
                } catch (Exception e) {
                    error("Erro ao salvar: " + e.getMessage());
                    target.add(feedback);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                target.add(feedback);
                target.add(form);
            }
        });

        form.add(new AjaxButton("cancelButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                onCancel(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                onCancel(target);
            }
        }.setDefaultFormProcessing(false));
    }

    protected void onSave(AjaxRequestTarget target) {
        Endereco endereco = (Endereco) getDefaultModelObject();
        callback.onSave(endereco);
    }

    protected abstract void onCancel(AjaxRequestTarget target);

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(new PackageResourceReference(NewAddressModal.class, "./AddressModal.css")));
    }
}