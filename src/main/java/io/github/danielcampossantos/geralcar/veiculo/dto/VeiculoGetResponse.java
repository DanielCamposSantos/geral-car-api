package io.github.danielcampossantos.geralcar.veiculo.dto;


import io.github.danielcampossantos.geralcar.imagem.dto.ImagemGetResponse;

import java.util.List;

public record VeiculoGetResponse(
        Long id,
        String marca,
        String modelo,
        String cor,
        Integer ano,
        Long quilometragem,
        String descricao,
        Double avaliacao,
        List<ImagemGetResponse> imagens

) {
}
