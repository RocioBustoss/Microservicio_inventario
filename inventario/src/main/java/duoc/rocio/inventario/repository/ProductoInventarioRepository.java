package duoc.rocio.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import duoc.rocio.inventario.model.ProductoInventario;
import java.util.List;


@Repository
public interface ProductoInventarioRepository extends JpaRepository<ProductoInventario, Long> {

    List<ProductoInventario> findByNombreProInvContainingIgnoreCase(String nombreProInv);
    
    List<ProductoInventario> findByEstadoProd(String estadoProd);

    List<ProductoInventario> findByStockActualLessThanEqual(int stockActual);

    boolean existsByCodigoSku(String codigoSku);

    
}
