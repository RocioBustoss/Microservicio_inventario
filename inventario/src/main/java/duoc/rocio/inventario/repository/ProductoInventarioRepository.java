package duoc.rocio.inventario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import duoc.rocio.inventario.model.ProductoInventario;

@Repository
public interface ProductoInventarioRepository extends JpaRepository<ProductoInventario, Long> {
    
    List<ProductoInventario> findByInventarioIdInventario(Long idInventario);
    List<ProductoInventario> findByInventarioIdInventarioAndNombreProInvContainingIgnoreCase(Long idInventario, String nombreProInv);
    List<ProductoInventario> findByInventarioIdInventarioAndEstadoProdContainingIgnoreCase(Long idInventario, String estadoProd);
    List<ProductoInventario> findByInventarioIdInventarioAndStockActualLessThanEqual(Long idInventario, int umbral);
    boolean existsByInventarioIdInventarioAndCodigoSku(Long idInventario, String codigoSku);
    Optional<ProductoInventario> findByIdProductoAndInventarioIdInventario(Long idProducto, Long idInventario);
    Optional<ProductoInventario> findByIdProductoAndInventario_IdInventario(Long idProducto, Long idInventario);
    

}
