package duoc.rocio.inventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import duoc.rocio.inventario.model.MovimientoInventario;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    // Obtener todos los movimientos ordenados desde el más reciente
    List<MovimientoInventario> findAllByOrderByFechaDesc();

    // Obtener movimientos de un producto específico
    List<MovimientoInventario> findByProducto_IdProductoOrderByFechaDesc(Long idProducto);

    // Obtener movimientos según tipo
    List<MovimientoInventario> findByTipoMovimientoIgnoreCaseOrderByFechaDesc(String tipoMovimiento);

    // Obtener movimientos realizados por un empleado
    List<MovimientoInventario> findByIdResponsableOrderByFechaDesc(Long idResponsable);
}