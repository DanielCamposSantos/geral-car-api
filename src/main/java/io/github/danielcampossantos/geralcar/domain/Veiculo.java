package io.github.danielcampossantos.geralcar.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String marca;
    @Column(nullable = false)
    private String modelo;
    @Column(nullable = false)
    private String cor;
    @Column(nullable = false)
    private Integer ano;
    @Column(nullable = false)
    private Long quilometragem;
    @Column(nullable = false)
    private String descricao;
    @Column(nullable = false)
    private Double avaliacao;
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private TipoCombustivel combustivel;
    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<Imagem> imagens = new ArrayList<>();

}
