package io.github.danielcampossantos.geralcar.utils;

import io.github.danielcampossantos.geralcar.domain.TipoCombustivel;
import io.github.danielcampossantos.geralcar.domain.Veiculo;

import java.util.ArrayList;

public class VeiculoUtils {
    public Veiculo generateVeiculo() {
        return Veiculo.builder()
                .id(1L)
                .marca("Toyota")
                .modelo("Corolla")
                .cor("Preto")
                .ano(2023)
                .quilometragem(15000L)
                .descricao("Veículo em excelente estado, único dono")
                .avaliacao(4.5)
                .combustivel(TipoCombustivel.FLEX)
                .imagens(new ArrayList<>())
                .build();
    }

}
