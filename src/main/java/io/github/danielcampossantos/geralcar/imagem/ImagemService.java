package io.github.danielcampossantos.geralcar.imagem;

import io.github.danielcampossantos.geralcar.domain.Imagem;
import io.github.danielcampossantos.geralcar.domain.Veiculo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagemService {
    private final ImagemRepository repository;
    private final FileStorageService service;


    public List<Imagem> saveImages(List<MultipartFile> images, Veiculo veiculo) throws IOException {
        List<Imagem> imagems = new ArrayList<>();

        for (MultipartFile file : images) {
            String path = service.storeImage(file, veiculo);
            var imagem = setImagemAttributes(path, veiculo);
            imagems.add(imagem);
        }

        return repository.saveAll(imagems);
    }


    public void deleteVeiculoImage(Veiculo veiculo) {
        service.deleteImageFolder(veiculo.getId());
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
