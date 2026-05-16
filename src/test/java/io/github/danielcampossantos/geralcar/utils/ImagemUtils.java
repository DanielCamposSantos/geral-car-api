package io.github.danielcampossantos.geralcar.utils;

import io.github.danielcampossantos.geralcar.domain.Imagem;
import io.github.danielcampossantos.geralcar.domain.Veiculo;

import java.util.ArrayList;
import java.util.List;

public class ImagemUtils {

    public Imagem generateImagem(Long id, String fileName, String imagePath,Veiculo veiculo) {
        var imagem = new Imagem();
        imagem.setId(id);
        imagem.setFileName(fileName);
        imagem.setImagePath(imagePath);
        imagem.setVeiculo(veiculo);
        return imagem;
    }


    public List<Imagem> generateImagensList() {
        var imagens = new ArrayList<Imagem>();

        var imagem1 = new Imagem();
        imagem1.setId(1L);
        imagem1.setFileName("foto1.jpg");
        imagem1.setImagePath("veiculos/1/foto1.jpg");

        var imagem2 = new Imagem();
        imagem2.setId(2L);
        imagem2.setFileName("foto2.jpg");
        imagem2.setImagePath("veiculos/1/foto2.jpg");

        imagens.add(imagem1);
        imagens.add(imagem2);

        return imagens;
    }

    public List<Imagem> generateImagensListForVeiculo(Veiculo veiculo) {
        var imagens = new ArrayList<Imagem>();

        var imagem1 = new Imagem();
        imagem1.setId(1L);
        imagem1.setFileName("foto1.jpg");
        imagem1.setImagePath("veiculos/" + veiculo.getId() + "/foto1.jpg");
        imagem1.setVeiculo(veiculo);

        var imagem2 = new Imagem();
        imagem2.setId(2L);
        imagem2.setFileName("foto2.jpg");
        imagem2.setImagePath("veiculos/" + veiculo.getId() + "/foto2.jpg");
        imagem2.setVeiculo(veiculo);

        imagens.add(imagem1);
        imagens.add(imagem2);

        return imagens;
    }
}