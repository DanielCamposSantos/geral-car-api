package io.github.danielcampossantos.geralcar.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
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
    @Enumerated(EnumType.STRING)
    private TipoCombustivel combustivel;
    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagem> imagens = new ArrayList<>();

}
