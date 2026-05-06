package io.github.danielcampossantos.geralcar.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoCombustivel {
    GASOLINA,
    ETANOL,
    FLEX,
    DIESEL,
    GNV,
    ELETRICO;

    @JsonCreator
    public static TipoCombustivel fromString(String key) {
        return TipoCombustivel.valueOf(key.toUpperCase());
    }
}
