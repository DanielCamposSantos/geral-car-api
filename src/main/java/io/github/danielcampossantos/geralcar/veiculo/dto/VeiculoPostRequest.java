package io.github.danielcampossantos.geralcar.veiculo.dto;

import io.github.danielcampossantos.geralcar.domain.TipoCombustivel;
import jakarta.validation.constraints.*;

public record VeiculoPostRequest(
        @NotEmpty(message = "A marca é obrigatória")
        String marca,

        @NotEmpty(message = "O modelo é obrigatório")
        String modelo,

        @NotEmpty(message = "A cor é obrigatória")
        String cor,

        @NotNull(message = "O ano é obrigatório")
        @Min(1900)
        @Max(2100)
        Integer ano,

        @NotNull(message = "A quilometragem é obrigatória")
        @PositiveOrZero(message = "A quilometragem não pode ser negativa")
        Long quilometragem,


        @NotEmpty(message = "A descrição é obrigatória")
        String descricao,

        @NotNull(message = "O combustivel é obrigatório")
        TipoCombustivel combustivel,

        @NotNull(message = "A avaliação é obrigatória")
        @Min(value = 0)
        @Max(10)
        Double avaliacao
) {
}
