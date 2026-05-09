package io.github.danielcampossantos.geralcar.veiculo;

import io.github.danielcampossantos.geralcar.domain.Veiculo;
import io.github.danielcampossantos.geralcar.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VeiculoFinder {

    private final VeiculoRepository repository;

    public Veiculo findByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Veículo não existe"));
    }
}
