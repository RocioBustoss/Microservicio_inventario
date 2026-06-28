package duoc.rocio.inventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import duoc.rocio.inventario.model.Restock;

@Repository
public interface RestockRepository extends JpaRepository<Restock, Long> {

    List<Restock> findByEstadoIgnoreCase(String estado);

    List<Restock> findByIdSolicitante(Long idSolicitante);

    
}