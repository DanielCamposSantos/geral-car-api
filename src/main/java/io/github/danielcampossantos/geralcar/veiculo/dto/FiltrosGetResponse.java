package io.github.danielcampossantos.geralcar.veiculo.dto;

import io.github.danielcampossantos.geralcar.domain.TipoCombustivel;

import java.util.List;

public record FiltrosGetResponse(
        List<Integer> anos,
        List<TipoCombustivel> combustiveis
) {
}
