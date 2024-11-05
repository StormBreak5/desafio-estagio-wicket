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
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.resource.PackageResourceReference;

public class EditAddressModal extends Panel {
    FeedbackPanel feedback = new FeedbackPanel("feedback");

    public interface SaveAddressCallback {
        void onSave(Endereco endereco, AjaxRequestTarget target);
    }

    public EditAddressModal(String id, CompoundPropertyModel<Endereco> model, SaveAddressCallback callback) {
        super(id, model);

        feedback.setOutputMarkupId(true);
        add(new FeedbackPanel("feedback"));

        Form<Endereco> form = new Form<>("form", model);
        add(form);

        form.add(new TextField<>("logradouro").setRequired(true));
        form.add(new TextField<>("numero").setRequired(true));
        form.add(new TextField<>("complemento"));
        form.add(new TextField<>("bairro").setRequired(true));
        form.add(new TextField<>("cidade").setRequired(true));
        form.add(new TextField<>("uf").setRequired(true));
        form.add(new TextField<>("cep").setRequired(true));
        form.add(new TextField<>("telefone"));
        form.add(new CheckBox("enderecoPrincipal"));

        form.add(new AjaxButton("saveButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                Endereco endereco = form.getModelObject();
                callback.onSave(endereco, target);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                target.add(feedback);
            }
        });

        form.add(new AjaxButton("cancelButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                onCancel(target);
            }
        }.setDefaultFormProcessing(false));
    }

    protected void onCancel(AjaxRequestTarget target) {}

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(new PackageResourceReference(EditAddressModal.class, "./AddressModal.css")));
    }
}
