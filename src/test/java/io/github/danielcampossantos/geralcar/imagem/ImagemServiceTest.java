package io.github.danielcampossantos.geralcar.imagem;


import io.github.danielcampossantos.geralcar.domain.Imagem;
import io.github.danielcampossantos.geralcar.domain.Veiculo;
import io.github.danielcampossantos.geralcar.exception.BadRequestException;
import io.github.danielcampossantos.geralcar.utils.ImagemUtils;
import io.github.danielcampossantos.geralcar.utils.MultipartFilesUtil;
import io.github.danielcampossantos.geralcar.utils.VeiculoUtils;
import io.github.danielcampossantos.geralcar.veiculo.VeiculoFinder;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ImagemServiceTest {

    @Mock
    private ImagemRepository repository;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private VeiculoFinder veiculoFinder;

    @InjectMocks
    private ImagemService imagemService;

    @InjectMocks
    private VeiculoUtils veiculoUtils;

    @InjectMocks
    private ImagemUtils imagemUtils;

    @InjectMocks
    private MultipartFilesUtil multipartFilesUtil;

    private Veiculo veiculo;
    private List<Imagem> imagensSalvas;
    private List<MultipartFile> multipartFiles;

    @BeforeEach
    void setUp() {
        imagensSalvas = new ArrayList<>();
        imagensSalvas.addAll(imagemUtils.generateImagensList());

        multipartFiles = new ArrayList<>();
        multipartFiles.addAll(multipartFilesUtil.generateMultipartFiles());

        veiculo = veiculoUtils.generateVeiculo();

    }

    @Test
    @SneakyThrows
    @DisplayName("saveImages() saves multiple images when successful")
    void saveImages_SavesMultipleImages_WhenSuccessful() {
        var firstSavedImagePath = imagensSalvas.getFirst().getImagePath();
        var lastSavedImagePath = imagensSalvas.getLast().getImagePath();

        BDDMockito.when(veiculoFinder.findByIdOrThrow(anyLong())).thenReturn(veiculo);

        BDDMockito.when(fileStorageService.storeImage(any(MockMultipartFile.class), any(Veiculo.class)))
                .thenReturn(firstSavedImagePath, lastSavedImagePath);

        BDDMockito.when(repository.saveAll(anyList())).thenReturn(imagensSalvas);

        var result = imagemService.saveImages(multipartFiles, 1L);

        Assertions.assertThat(result)
                .isNotNull()
                .hasSize(2)
                .isEqualTo(imagensSalvas);

        BDDMockito.verify(veiculoFinder, times(1)).findByIdOrThrow(1L);
        BDDMockito.verify(fileStorageService, times(2)).storeImage(any(MultipartFile.class), eq(veiculo));
        BDDMockito.verify(repository, times(1)).saveAll(anyList());
    }

    @Test
    @SneakyThrows
    @DisplayName("saveImages() throws exception when veiculo not found")
    void saveImages_ThrowsException_WhenVeiculoNotFound() {
        var exceptionMessage = "Veiculo não encontrado";
        var id = 999L;

        BDDMockito.when(veiculoFinder.findByIdOrThrow(anyLong()))
                .thenThrow(new BadRequestException(exceptionMessage));

        Assertions.assertThatException()
                .isThrownBy(() -> imagemService.saveImages(multipartFiles, id))
                .isInstanceOf(BadRequestException.class)
                .withMessageContaining(exceptionMessage);

        BDDMockito.verify(fileStorageService, never()).storeImage(any(), any());
        BDDMockito.verify(repository, never()).saveAll(any());
    }

    @Test
    @SneakyThrows
    @DisplayName("saveImages() calls storeImage for each file")
    void saveImages_CallsStoreImageForEachFile_WhenSuccessful() {
        var firstSavedImagePath = imagensSalvas.getFirst().getImagePath();
        var lastSavedImagePath = imagensSalvas.getLast().getImagePath();
        var id = 1L;

        BDDMockito.when(veiculoFinder.findByIdOrThrow(anyLong())).thenReturn(veiculo);

        BDDMockito.when(fileStorageService.storeImage(any(MockMultipartFile.class), any(Veiculo.class)))
                .thenReturn(firstSavedImagePath, lastSavedImagePath);

        BDDMockito.when(repository.saveAll(anyList())).thenReturn(Collections.emptyList());

        imagemService.saveImages(multipartFiles, id);

        BDDMockito.verify(fileStorageService, times(2))
                .storeImage(any(MultipartFile.class), eq(veiculo));
    }

    @Test
    @DisplayName("saveImages() saves correct image attributes")
    @SneakyThrows
    void saveImages_SavesCorrectImageAttributes_WhenSuccessful() {
        var expectedImageName = "Toyota_Corolla_Preto_1.jpg";
        var expectedImagePath = "veiculos/1/Toyota_Corolla_Preto_1.jpg";
        var imagemSalva = imagemUtils.generateImagem(1L, expectedImageName, expectedImagePath, veiculo);

        BDDMockito.when(veiculoFinder.findByIdOrThrow(anyLong())).thenReturn(veiculo);

        BDDMockito.when(fileStorageService.storeImage(any(MultipartFile.class), any(Veiculo.class)))
                .thenReturn(expectedImagePath);

        BDDMockito.when(repository.saveAll(anyList())).thenReturn(List.of(imagemSalva));

        var result = imagemService.saveImages(List.of(multipartFiles.getFirst()), 1L);
        var firstResult = result.getFirst();

        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(firstResult.getImagePath()).isEqualTo(expectedImagePath);
        Assertions.assertThat(firstResult.getVeiculo()).isEqualTo(veiculo);
        Assertions.assertThat(firstResult.getFileName()).isEqualTo(expectedImageName);

        BDDMockito.verify(repository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("deleteVeiculoImages() deletes all images from veiculo when successful")
    void deleteVeiculoImages_DeletesAllImages_WhenSuccessful() {
        var id = 1L;

        BDDMockito.when(veiculoFinder.findByIdOrThrow(anyLong())).thenReturn(veiculo);
        BDDMockito.doNothing().when(fileStorageService).deleteImageFolder(anyLong());

        imagemService.deleteVeiculoImages(id);

        BDDMockito.verify(veiculoFinder, times(1)).findByIdOrThrow(id);
        BDDMockito.verify(fileStorageService, times(1)).deleteImageFolder(id);
    }

    @Test
    @DisplayName("deleteVeiculoImages() throws exception when veiculo not found")
    void deleteVeiculoImages_ThrowsException_WhenVeiculoNotFound() {
        var id = 999L;

        BDDMockito.when(veiculoFinder.findByIdOrThrow(anyLong()))
                .thenThrow(new BadRequestException("Veículo não encontrado"));

        Assertions.assertThatException()
                .isThrownBy(() -> imagemService.deleteVeiculoImages(id))
                .isInstanceOf(BadRequestException.class)
                .withMessageContaining("Veículo não encontrado");

        BDDMockito.verify(fileStorageService, never()).deleteImageFolder(anyLong());
    }

    @Test
    @DisplayName("deleteVeiculoImage() throws exception when image not found")
    @SneakyThrows
    void deleteVeiculoImage_ThrowsException_WhenImageNotFound() {
        var imagemExistente = imagemUtils.generateImagem(2L, "foto2.jpg", "veiculos/1/foto2.jpg", veiculo);
        var imagens = new ArrayList<>(List.of(imagemExistente));

        veiculo.setImagens(imagens);

        BDDMockito.when(veiculoFinder.findByIdOrThrow(anyLong())).thenReturn(veiculo);

        Assertions.assertThatException()
                .isThrownBy(() -> imagemService.deleteVeiculoImage(1L, 999L))
                .isInstanceOf(BadRequestException.class)
                .withMessageContaining("Imagem não encontrada");
    }

    @Test
    @DisplayName("deleteVeiculoImage() throws exception when veiculo not found")
    @SneakyThrows
    void deleteVeiculoImage_ThrowsException_WhenVeiculoNotFound() {
        var exceptionMessage = "Veículo não encontrado";
        var veiculoId = 999L;
        var imagemId = 1L;

        BDDMockito.when(veiculoFinder.findByIdOrThrow(anyLong()))
                .thenThrow(new BadRequestException(exceptionMessage));

        Assertions.assertThatException()
                .isThrownBy(() -> imagemService.deleteVeiculoImage(veiculoId, imagemId))
                .isInstanceOf(BadRequestException.class)
                .withMessageContaining(exceptionMessage);

    }
}