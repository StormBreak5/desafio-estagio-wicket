package com.desafiowicket.pages;

import org.apache.wicket.model.Model;
import org.apache.wicket.markup.html.basic.Label;

public class ClientList extends BasePage {
    private static final long serialVersionUID = 502327979204314267L;

    public ClientList() {
        Label teste = new Label("teste", Model.of("Isso é um teste"));
        add(teste);
    }
}
