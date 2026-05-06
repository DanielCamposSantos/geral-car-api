package io.github.danielcampossantos.geralcar.imagem;

import io.github.danielcampossantos.geralcar.domain.Imagem;
import io.github.danielcampossantos.geralcar.imagem.dto.ImagemGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ImagemMapper {
    ImagemGetResponse toImagemGetResponse(Imagem imagem);
}
