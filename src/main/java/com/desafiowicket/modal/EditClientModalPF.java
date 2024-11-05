package com.desafiowicket.modal;

import com.desafiowicket.model.ClienteForm;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.PackageResourceReference;

public abstract class EditClientModalPF extends Panel {
    public EditClientModalPF(String id, IModel<ClienteForm> model) {
        super(id, model);

        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        final Form<ClienteForm> form = new Form<>("form", model);
        form.setOutputMarkupId(true);
        add(form);

        form.add(new TextField<>("nome").setRequired(true));
        form.add(new TextField<>("cpf", new PropertyModel<String>(model, "cpfCnpj")).setRequired(true));
        form.add(new TextField<>("rg"));
        form.add(new DateTextField("dataNascimento", "yyyy-MM-dd").setRequired(true));
        form.add(new TextField<>("emailPF", new PropertyModel<String>(model, "email")).setRequired(true));
        form.add(new CheckBox("ativo"));

        form.add(new AjaxButton("saveButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                try {
                    onSave(target);
                } catch (Exception e) {
                    error("Erro ao salvar: " + e.getMessage());
                    target.add(feedbackPanel);
                }
            }
            @Override
            protected void onError(AjaxRequestTarget target) {
                target.add(feedbackPanel);
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

    protected abstract void onSave(AjaxRequestTarget target);
    protected abstract void onCancel(AjaxRequestTarget target);

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(new PackageResourceReference(EditClientModalPF.class, "./EditClientModal.css")));
    }
}
