package io.github.danielcampossantos.geralcar.imagem;

import io.github.danielcampossantos.geralcar.domain.Imagem;
import io.github.danielcampossantos.geralcar.domain.Veiculo;
import io.github.danielcampossantos.geralcar.exception.BadRequestException;
import io.github.danielcampossantos.geralcar.veiculo.VeiculoFinder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagemService {
    private final ImagemRepository repository;
    private final FileStorageService service;
    private final VeiculoFinder veiculoFinder;

    @SneakyThrows
    public List<Imagem> saveImages(List<MultipartFile> images, Long veiculoId) {
        List<Imagem> imagens = new ArrayList<>();

        var savedVeiculo = veiculoFinder.findByIdOrThrow(veiculoId);

        addImagesToArrayList(images, savedVeiculo, imagens);

        return repository.saveAll(imagens);
    }

    public void deleteVeiculoImages(Long id) {
        var veiculoToDelete = veiculoFinder.findByIdOrThrow(id);
        service.deleteImageFolder(veiculoToDelete.getId());
    }

    public void deleteVeiculoImage(Long veiculoId, Long imagemId) {
        var target = veiculoFinder.findByIdOrThrow(veiculoId);
        var imagens = target.getImagens();
        var imageToDelete = findImageByIdOrThrowBadRequest(imagemId, imagens);

        imagens.remove(imageToDelete);
        deleteImageFromFolder(imageToDelete);
        repository.delete(imageToDelete);
    }



    private void addImagesToArrayList(List<MultipartFile> images, Veiculo savedVeiculo, List<Imagem> imagens) throws IOException {
        for (MultipartFile file : images) {
            String path = service.storeImage(file, savedVeiculo);
            var imagem = setImagemAttributes(path, savedVeiculo);
            imagens.add(imagem);
        }
    }

    private static void deleteImageFromFolder(Imagem savedImagem) {
        try {
            FileStorageService.deleteCurrentFile(Path.of(savedImagem.getImagePath()));
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    private static Imagem findImageByIdOrThrowBadRequest(Long imagemId, List<Imagem> imagens) {
        return imagens.stream().filter(imagem -> imagem.getId().equals(imagemId))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Imagem não encontrada"));
    }

    private Imagem setImagemAttributes(String path, Veiculo veiculo) {
        var imagem = new Imagem();
        imagem.setFileName(getFileName(path));
        imagem.setImagePath(path);
        imagem.setVeiculo(veiculo);
        return imagem;
    }

    private static String getFileName(String path) {
        return Paths.get(path).toFile().getName();
    }

}
