package io.github.danielcampossantos.geralcar.veiculo;

import io.github.danielcampossantos.geralcar.domain.Veiculo;
import io.github.danielcampossantos.geralcar.imagem.ImagemMapper;
import io.github.danielcampossantos.geralcar.veiculo.dto.VeiculoPostRequest;
import io.github.danielcampossantos.geralcar.veiculo.dto.VeiculoGetResponse;
import io.github.danielcampossantos.geralcar.veiculo.dto.VeiculoPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ImagemMapper.class})
public interface VeiculoMapper {
    VeiculoGetResponse toVeiculoGetResponse(Veiculo veiculo);

    Veiculo toVeiculo(VeiculoPostRequest veiculoPostRequest);

    VeiculoPostResponse toVeiculoPostResponse(Veiculo veiculo);
}
