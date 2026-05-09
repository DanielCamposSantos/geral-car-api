package io.github.danielcampossantos.geralcar.imagem;

import io.github.danielcampossantos.geralcar.domain.Veiculo;
import lombok.SneakyThrows;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeImage(MultipartFile file, Veiculo veiculo) throws IOException {
        var veiculoInfo = generateVeiculoInfo(veiculo);
        var fileName = generateFileName(file, veiculo.getId(), veiculoInfo);

        Path veiculoPath = Paths.get(uploadDir, "veiculos", veiculo.getId().toString());
        Files.createDirectories(veiculoPath);

        Path savedImagePath = veiculoPath.resolve(fileName);

        Files.copy(file.getInputStream(), savedImagePath, StandardCopyOption.REPLACE_EXISTING);

        return savedImagePath.toString().replace("\\", "/");

    }


    @SneakyThrows
    public void deleteImageFolder(Long veiculoId) {
        Path veiculoPath = Paths.get(uploadDir, "veiculos", veiculoId.toString());

        if (Files.exists(veiculoPath)) {
            deleteFilesInFolder(veiculoPath);
            deleteCurrentFile(veiculoPath);
        }
    }

    private static void deleteFilesInFolder(Path veiculoPath) throws IOException {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(veiculoPath)) {
            for (Path file : directoryStream) {
                deleteCurrentFile(file);
            }
        }
    }

    public static void deleteCurrentFile(Path veiculoPath) throws IOException {
        Files.delete(veiculoPath);
    }


    private static String generateFileName(MultipartFile file, Long veiculoId, String veiculoInfo) {
        return veiculoInfo + "_" + veiculoId.toString() + getFileExtension(file);
    }

    private static String getFileExtension(MultipartFile file) {
        var originalFilename = file.getOriginalFilename();
        int i = Objects.requireNonNull(originalFilename).lastIndexOf('.');
        return originalFilename.substring(i);
    }


    private static @NonNull String generateVeiculoInfo(Veiculo veiculo) {
        return "%s-%s_%s_%s".formatted(
                UUID.randomUUID().toString(),
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getCor());
    }

}
