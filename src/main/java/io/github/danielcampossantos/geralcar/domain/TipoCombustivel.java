package io.github.danielcampossantos.geralcar.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoCombustivel {
    GASOLINA,
    ETANOL,
    FLEX,
    DIESEL,
    GNV,
    ELETRICO,
    HIBRIDO;

    @JsonCreator
    public static TipoCombustivel fromString(String key) {
        if (key == null) return null;
        for (TipoCombustivel tipo : TipoCombustivel.values()) {
            if (tipo.name().equalsIgnoreCase(key)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Combustível inválido: " + key);
    }
}