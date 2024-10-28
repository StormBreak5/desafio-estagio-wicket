package com.desafiowicket.model;

public enum TipoPessoa {
    FISICA("Física"), JURIDICA("Jurídica");
    private final String label;

    private TipoPessoa(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
