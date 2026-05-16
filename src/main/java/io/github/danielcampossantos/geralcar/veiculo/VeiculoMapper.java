package io.github.danielcampossantos.geralcar.veiculo;

import io.github.danielcampossantos.geralcar.domain.Veiculo;
import io.github.danielcampossantos.geralcar.imagem.ImagemMapper;
import io.github.danielcampossantos.geralcar.veiculo.dto.VeiculoGetResponse;
import io.github.danielcampossantos.geralcar.veiculo.dto.VeiculoPostRequest;
import io.github.danielcampossantos.geralcar.veiculo.dto.VeiculoPostResponse;
import io.github.danielcampossantos.geralcar.veiculo.dto.VeiculoPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ImagemMapper.class})
public interface VeiculoMapper {

    Veiculo toVeiculo(VeiculoPutRequest request);

    Veiculo toVeiculo(VeiculoPostRequest veiculoPostRequest);

    VeiculoGetResponse toVeiculoGetResponse(Veiculo veiculo);

    List<VeiculoGetResponse> toVeiculoGetResponseList(List<Veiculo> veiculo);

    VeiculoPostResponse toVeiculoPostResponse(Veiculo veiculo);
}
