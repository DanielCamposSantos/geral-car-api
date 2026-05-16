package io.github.danielcampossantos.geralcar.veiculo;

import io.github.danielcampossantos.geralcar.domain.TipoCombustivel;
import io.github.danielcampossantos.geralcar.domain.Veiculo;
import io.github.danielcampossantos.geralcar.exception.BadRequestException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VeiculoSpecs {

    public static Specification<Veiculo> filterMarca(String marca) {
        return (root, query, builder) ->
                Optional.ofNullable(marca)
                        .filter(s -> !s.isBlank())
                        .map(valor -> builder.equal(root.get("marca"), valor))
                        .orElseGet(builder::conjunction);
    }

    public static Specification<Veiculo> filterModelo(String modelo) {
        return (root, query, builder) ->
                Optional.ofNullable(modelo)
                        .filter(s -> !s.isBlank())
                        .map(valor -> builder.equal(root.get("modelo"), valor))
                        .orElseGet(builder::conjunction);
    }

    public static Specification<Veiculo> filterCombustivel(String combustivel) {
        return (root, query, builder) ->
                Optional.ofNullable(combustivel)
                        .filter(s -> !s.isBlank())
                        .map(String::toUpperCase)
                        .map(VeiculoSpecs::conversorDeEnumCombustivel)
                        .map(valor -> builder.equal(root.get("combustivel"), valor))
                        .orElseGet(builder::conjunction);
    }

    public static Specification<Veiculo> filterAno(Integer ano) {
        return (root, query, builder) ->
                Optional.ofNullable(ano)
                        .map(a -> builder.equal(root.get("ano"), a))
                        .orElseGet(builder::conjunction);
    }


    private static Enum<TipoCombustivel> conversorDeEnumCombustivel(String combustivel) {
        if (combustivel == null) throw new BadRequestException("Combustível inválido: " + combustivel);

        return TipoCombustivel.valueOf(combustivel.toUpperCase());
    }


}
