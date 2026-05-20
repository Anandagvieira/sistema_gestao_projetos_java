package com.eventos.model;

/**
 * Categorias permitidas para cadastro de eventos na cidade.
 */
public enum CategoriaEvento {
    FESTA("Festas"),
    ESPORTIVO("Eventos esportivos"),
    SHOW("Shows"),
    CULTURAL("Eventos culturais"),
    FEIRA("Feiras e exposições"),
    RELIGIOSO("Eventos religiosos"),
    EDUCACIONAL("Eventos educacionais"),
    GASTRONOMICO("Eventos gastronômicos"),
    OUTROS("Outros");

    private final String descricao;

    CategoriaEvento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static CategoriaEvento fromOrdinal(int ordinal) {
        CategoriaEvento[] valores = values();
        if (ordinal < 0 || ordinal >= valores.length) {
            throw new IllegalArgumentException("Categoria inválida: " + ordinal);
        }
        return valores[ordinal];
    }
}
