package com.desafiowicket.modal;

import com.desafiowicket.model.ClienteForm;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public abstract class EditClientModal extends Panel {
    public EditClientModal(String id, IModel<ClienteForm> model) {
        super(id, model);

        Form<ClienteForm> form = new Form<>("form", model);
        add(form);

        form.add(new TextField<>("nome"));
        form.add(new TextField<>("cpf"));
        form.add(new TextField<>("rg"));
        form.add(new TextField<>("dataNascimento"));
        form.add(new TextField<>("email"));

        form.add(new AjaxButton("saveButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                onSave(target);
            }
        });
    }

    protected abstract void onSave(AjaxRequestTarget target);
}
