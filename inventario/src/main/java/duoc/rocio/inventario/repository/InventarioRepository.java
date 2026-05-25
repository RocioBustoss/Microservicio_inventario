package duoc.rocio.inventario.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import duoc.rocio.inventario.model.Inventario;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long>{
    
    List<Inventario> findByIdTienda(Long idTienda);
    boolean existsByIdTienda(Long idTienda);
    
}

