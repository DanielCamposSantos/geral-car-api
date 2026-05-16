package io.github.danielcampossantos.geralcar.veiculo;

import io.github.danielcampossantos.geralcar.domain.TipoCombustivel;
import io.github.danielcampossantos.geralcar.exception.BadRequestException;
import io.github.danielcampossantos.geralcar.imagem.ImagemService;
import io.github.danielcampossantos.geralcar.veiculo.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class VeiculoService {
    private final VeiculoRepository repository;
    private final VeiculoMapper veiculoMapper;
    private final ImagemService imagemService;
    private final VeiculoFinder veiculoFinder;

    public Page<VeiculoGetResponse> findAll(String marca,
                                            String modelo,
                                            Integer ano,
                                            String combustivel,
                                            Pageable pageable) {

        var filters = Specification
                .where(VeiculoSpecs.filterMarca(marca))
                .and(VeiculoSpecs.filterModelo(modelo))
                .and(VeiculoSpecs.filterCombustivel(combustivel))
                .and(VeiculoSpecs.filterAno(ano));

        var paginatedVeiculos = repository.findAll(filters, pageable);

        return paginatedVeiculos.map(veiculoMapper::toVeiculoGetResponse);
    }

    public VeiculoGetResponse findByIdOrThrowBadRequestException(Long id) {
        var veiculoFoundById = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Veículo não encontrado com id informado."));

        return veiculoMapper.toVeiculoGetResponse(veiculoFoundById);
    }

    public FiltrosGetResponse getFiltros() {
        var marcas = repository.getAllMarcasDistinct();
        var modelos = repository.getAllModelosDistinct();
        var anos = repository.getAllAnosDistinct();
        var combustiveis = List.of(TipoCombustivel.values());
        return new FiltrosGetResponse(marcas, modelos, anos, combustiveis);
    }

    @SneakyThrows
    public VeiculoPostResponse save(VeiculoPostRequest request, List<MultipartFile> images) {
        var veiculoToSave = veiculoMapper.toVeiculo(request);

        var savedVeiculo = repository.save(veiculoToSave);

        var savedImages = imagemService.saveImages(images, savedVeiculo.getId());

        savedVeiculo.getImagens().addAll(savedImages);

        return veiculoMapper.toVeiculoPostResponse(savedVeiculo);
    }


    public void update(VeiculoPutRequest request) {
        var veiculoToUpdate = veiculoMapper.toVeiculo(request);
        repository.save(veiculoFinder.findByIdOrThrow(veiculoToUpdate.getId()));
    }


    public void deleteById(Long id) {
        var veiculoToDelete = veiculoFinder.findByIdOrThrow(id);
        imagemService.deleteVeiculoImages(veiculoToDelete.getId());
        repository.deleteById(veiculoToDelete.getId());
    }

    public List<VeiculoGetResponse> findAllDestaque() {
        return veiculoMapper.toVeiculoGetResponseList(repository.getVeiculosByDestaqueIsTrue());

    }

    public void toggleDestaque(Long id, Boolean destaque) {
        var veiculoToPatch = veiculoFinder.findByIdOrThrow(id);
        veiculoToPatch.setDestaque(destaque);
        repository.save(veiculoToPatch);
    }

    public Integer getDestaqueCount() {
        return repository.countVeiculosByDestaqueIsTrue();
    }

}
