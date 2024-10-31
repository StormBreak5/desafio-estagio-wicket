package com.desafiowicket.modal;

import com.desafiowicket.model.ClienteForm;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public abstract class DeleteConfirmationModal extends Panel {
    public DeleteConfirmationModal(String id, IModel<ClienteForm> model) {
        super(id, model);

        Form<ClienteForm> form = new Form<>("form", model);
        form.setOutputMarkupId(true);
        add(form);

        form.add(new AjaxButton("confirmButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                onConfirm(target);
            }
        });

        form.add(new AjaxButton("cancelButton") {
           protected void onSubmit(AjaxRequestTarget target) {
               onCancel(target);
           }
        }.setDefaultFormProcessing(false));
    }

    protected abstract void onConfirm(AjaxRequestTarget target);
    protected abstract void onCancel(AjaxRequestTarget target);
}
