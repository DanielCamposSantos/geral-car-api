package io.github.danielcampossantos.geralcar.imagem;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("veiculos/{id}/imagens")
@RequiredArgsConstructor
public class ImagemController {

    private final ImagemService imagemService;

    @PostMapping
    public ResponseEntity<Void> addImages(@RequestPart(name = "images") List<MultipartFile> images, @PathVariable Long id) {
        imagemService.saveImages(images, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteImages(@PathVariable(name = "id") Long veiculoId, @RequestParam Long imagemId) {
        imagemService.deleteVeiculoImage(veiculoId, imagemId);
        return ResponseEntity.noContent().build();
    }

}
