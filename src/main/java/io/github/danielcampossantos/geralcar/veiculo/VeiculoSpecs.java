package io.github.danielcampossantos.geralcar.veiculo;

import io.github.danielcampossantos.geralcar.domain.Veiculo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VeiculoSpecs {

    public static Specification<Veiculo> filterCombustivel(String combustivel) {
        return (root, query, builder) ->
                Optional.ofNullable(combustivel)
                        .filter(s -> !s.isBlank())
                        .map(String::toUpperCase)
                        .map(valor -> builder.equal(root.get("combustivel"), valor))
                        .orElseGet(builder::conjunction);

    }

    public static Specification<Veiculo> filterAno(Integer ano) {
        return (root, query, builder) ->
                Optional.ofNullable(ano)
                        .map(a -> builder.equal(root.get("ano"), a))
                        .orElseGet(builder::conjunction);

    }
}
