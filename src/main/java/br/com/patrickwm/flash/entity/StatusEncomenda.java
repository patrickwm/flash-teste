package br.com.patrickwm.flash.entity;

public enum StatusEncomenda {
    POSTADO("Postado"), EM_TRANSITO("Em trânsito"), ENTREGUE("Entregue");

    private final String descricao;

    private StatusEncomenda(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
