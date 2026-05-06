package io.github.danielcampossantos.geralcar.veiculo;

import io.github.danielcampossantos.geralcar.domain.Veiculo;
import io.github.danielcampossantos.geralcar.exception.BadRequestException;
import io.github.danielcampossantos.geralcar.imagem.ImagemService;
import io.github.danielcampossantos.geralcar.veiculo.dto.VeiculoPostRequest;
import io.github.danielcampossantos.geralcar.veiculo.dto.VeiculoGetResponse;
import io.github.danielcampossantos.geralcar.veiculo.dto.VeiculoPostResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VeiculoService {
    private final VeiculoRepository repository;
    private final VeiculoMapper veiculoMapper;
    private final ImagemService imagemService;


    public Page<VeiculoGetResponse> findAllPage(Pageable pageable) {
        var paginatedVeiculos = repository.findAll(pageable);
        return paginatedVeiculos.map(veiculoMapper::toVeiculoGetResponse);
    }

    public VeiculoGetResponse findByIdOrThrowBadRequestException(Long id) {
        var veiculoFoundById = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Veículo não encontrado com id informado."));

        return veiculoMapper.toVeiculoGetResponse(veiculoFoundById);
    }

    @SneakyThrows
    public VeiculoPostResponse save(VeiculoPostRequest request, List<MultipartFile> images) {
        var veiculoToSave = veiculoMapper.toVeiculo(request);

        var savedVeiculo = repository.save(veiculoToSave);

        var savedImages = imagemService.saveImages(images, savedVeiculo);

        savedVeiculo.getImagens().addAll(savedImages);

        return veiculoMapper.toVeiculoPostResponse(savedVeiculo);
    }

    public void deleteById(Long id) {
        var veiculoToDelete = assertsVeiculoExists(id);
        repository.deleteById(veiculoToDelete.getId());
        imagemService.deleteVeiculoImage(veiculoToDelete);
    }

    public Veiculo assertsVeiculoExists(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Veículo não existe"));
    }


}
