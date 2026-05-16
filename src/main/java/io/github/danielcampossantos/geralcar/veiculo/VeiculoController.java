package io.github.danielcampossantos.geralcar.veiculo;

import io.github.danielcampossantos.geralcar.veiculo.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("veiculos")
@RequiredArgsConstructor
public class VeiculoController {
    private final VeiculoService service;

    @GetMapping
    public ResponseEntity<Page<VeiculoGetResponse>> findAll(
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) String combustivel,
            @PageableDefault(size = 6) Pageable pageable) {
        return ResponseEntity.ok(service.findAll(marca, modelo, ano, combustivel, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoGetResponse> findById(@PathVariable Long id) {
        var response = service.findByIdOrThrowBadRequestException(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filtros")
    public ResponseEntity<FiltrosGetResponse> getAllFiltros() {
        return ResponseEntity.ok(service.getFiltros());
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VeiculoPostResponse> save(@Valid @RequestPart("data") VeiculoPostRequest request,
                                                    @RequestPart("images") List<MultipartFile> images) {

        var response = service.save(request, images);
        return ResponseEntity.ok().body(response);
    }


    @PutMapping()
    public ResponseEntity<Void> update(@Valid @RequestBody VeiculoPutRequest request) {
        service.update(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/destaques")
    public ResponseEntity<List<VeiculoGetResponse>> findAllDestaque() {
        return ResponseEntity.ok().body(service.findAllDestaque());
    }

    @GetMapping("/destaques/count")
    public ResponseEntity<Integer> getDestaqueCount() {
        return ResponseEntity.ok(service.getDestaqueCount());
    }

    @PatchMapping("/destaques/{id}")
    public ResponseEntity<Void> toggleDestaque(@PathVariable Long id, @RequestParam Boolean destaque) {
        service.toggleDestaque(id, destaque);
        return ResponseEntity.noContent().build();
    }
}
