package io.github.danielcampossantos.geralcar.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Imagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;
}
