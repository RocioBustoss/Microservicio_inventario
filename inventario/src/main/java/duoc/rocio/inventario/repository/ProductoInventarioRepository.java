package duoc.rocio.inventario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import duoc.rocio.inventario.model.ProductoInventario;

@Repository
public interface ProductoInventarioRepository extends JpaRepository<ProductoInventario, Long> {
    
    Optional<ProductoInventario> findByNombreProd(String nombreProd);
    List<ProductoInventario> findByInventario_IdInventario(Long idInventario);
    boolean existsByInventario_IdInventarioAndCodigoSku(Long idInventario, String codigoSku);
    Optional<ProductoInventario> findByIdProductoAndInventario_IdInventario(Long idProducto, Long idInventario);
    List<ProductoInventario> findByInventario_IdInventarioAndStockActualLessThanEqual(Long idInventario, int umbral);
    List<ProductoInventario> findByInventario_IdInventarioAndNombreProdContainingIgnoreCase(Long idInventario, String nombreProd);

    

}
