package io.github.danielcampossantos.geralcar.veiculo;

import io.github.danielcampossantos.geralcar.veiculo.dto.VeiculoGetResponse;
import io.github.danielcampossantos.geralcar.veiculo.dto.VeiculoPostRequest;
import io.github.danielcampossantos.geralcar.veiculo.dto.VeiculoPostResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) String combustivel,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(ano, combustivel, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoGetResponse> findById(@PathVariable Long id) {
        var response = service.findByIdOrThrowBadRequestException(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VeiculoPostResponse> save(@Valid @RequestPart("data") VeiculoPostRequest request,
                                                    @RequestPart("images") List<MultipartFile> images) {

        var response = service.save(request, images);
        return ResponseEntity.ok().body(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
