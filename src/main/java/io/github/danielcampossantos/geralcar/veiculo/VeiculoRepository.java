package io.github.danielcampossantos.geralcar.veiculo;

import io.github.danielcampossantos.geralcar.domain.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long>, JpaSpecificationExecutor<Veiculo> {
    @Query("SELECT DISTINCT v.ano FROM Veiculo v ORDER BY v.ano")
    List<Integer> getAllAnosDistinct();

    @Query("SELECT DISTINCT v.marca FROM Veiculo v ORDER BY v.marca")
    List<String> getAllMarcasDistinct();

    @Query("SELECT DISTINCT v.modelo FROM Veiculo v ORDER BY v.modelo")
    List<String> getAllModelosDistinct();

    List<Veiculo> getVeiculosByDestaqueIsTrue();

    Integer countVeiculosByDestaqueIsTrue();

}
