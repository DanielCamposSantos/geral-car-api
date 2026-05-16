package io.github.danielcampossantos.geralcar.imagem;

import io.github.danielcampossantos.geralcar.domain.Veiculo;
import io.github.danielcampossantos.geralcar.utils.VeiculoUtils;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Path;

@ExtendWith(MockitoExtension.class)
class FileStorageServiceTest {
    @InjectMocks
    private FileStorageService service;
    @InjectMocks
    private VeiculoUtils utils;
    private Veiculo veiculo;
    @TempDir
    private Path tempDir;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "uploadDir", tempDir.toString());
        veiculo = utils.generateVeiculo();
    }

    @Test
    @SneakyThrows
    @DisplayName("storeImage() saves image in directory when successful")
    void storeImage_SavesImageInDirectory_WhenSuccessful() {
        var contentBytes = "fake image content".getBytes();
        var file = new MockMultipartFile(
                "file",
                "foto.jpg",
                "image/jpeg",
                contentBytes
        );

        var savedPath = service.storeImage(file, veiculo);
        Assertions.assertThat(savedPath).isNotNull();
        Assertions.assertThat(Path.of(savedPath)).exists();
        Assertions.assertThat(contentBytes).isNotEmpty().isEqualTo(file.getBytes());
    }

    @Test
    @SneakyThrows
    @DisplayName("storeImage() creates directory structure when does not exits")
    void storeImage_CreatesDirectoryStructure_WhenDoesNotExits() {
        var contentBytes = "fake image content".getBytes();
        var file = new MockMultipartFile(
                "file",
                "foto.jpg",
                "image/jpeg",
                contentBytes
        );

        service.storeImage(file, veiculo);
        var veiculoDir = tempDir.resolve("veiculos").resolve("1");
        Assertions.assertThat(veiculoDir)
                .exists()
                .isNotNull()
                .isDirectory();
    }

    @Test
    @SneakyThrows
    @DisplayName("storeImage() generates file name with correct extension when successful")
    void storeImage_GeneratesFileNameWithCorrectExtension_WhenSuccessful() {
        var contentBytes = "fake image content".getBytes();
        var file = new MockMultipartFile(
                "file",
                "foto.jpg",
                "image/jpeg",
                contentBytes
        );

        var savedPath = service.storeImage(file, veiculo);
        Assertions.assertThat(savedPath)
                .isNotEmpty()
                .isNotNull()
                .endsWith(".jpg")
                .contains("Toyota_Corolla_Preto_1.jpg");
    }

    @Test
    @DisplayName("storeImage replaces existing file when same path is used")
    void storeImage_ReplacesExistingFile_WhenSamePathIsUsed() throws IOException {
        var contentBytes1 = "fake image content 1".getBytes();
        var contentBytes2 = "fake image content 2".getBytes();

        var file = new MockMultipartFile(
                "file",
                "foto.jpg",
                "image/jpeg",
                contentBytes1
        );

        String path1 = service.storeImage(file, veiculo);
        var file2 = new MockMultipartFile(
                "file",
                "foto.jpg",
                "image/jpeg",
                contentBytes2
        );
        String path2 = service.storeImage(file2, veiculo);

        Assertions.assertThat(path1).isNotEqualTo(path2);

        Path existingPath1 = Path.of(path1);
        Path existingPath2 = Path.of(path2);


        Assertions.assertThat(existingPath1)
                .exists()
                .hasParentRaw(existingPath2.getParent());

        Assertions.assertThat(existingPath2).exists();

    }

    @Test
    @DisplayName("storeImage throws StringIndexOutOfBoundsException when file has no name")
    void storeImage_ThrowsStringIndexOutOfBoundsException_WhenFileHasNoName() {
        var contentBytes = "fake image content".getBytes();

        var file = new MockMultipartFile(
                "file",
                null,
                "image/jpeg",
                contentBytes
        );

        Assertions.assertThatException()
                .isThrownBy(() -> service.storeImage(file, veiculo))
                .isInstanceOf(StringIndexOutOfBoundsException.class);
    }

    @Test
    @SneakyThrows
    void deleteImageFolder_DeletesImageFolder_WhenSuccessful() {
        var contentBytes = "fake image content".getBytes();

        var file = new MockMultipartFile(
                "file",
                "foto.jpg",
                "image/jpeg",
                contentBytes
        );

        var savedPath = service.storeImage(file, veiculo);

        Assertions.assertThatNoException().isThrownBy(() -> service.deleteImageFolder(veiculo.getId()));
        Assertions.assertThat(Path.of(savedPath)).doesNotExist();
    }
}