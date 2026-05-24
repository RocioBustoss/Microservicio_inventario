package duoc.rocio.inventario.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import duoc.rocio.inventario.model.MovimientoInventario;
import duoc.rocio.inventario.model.ProductoInventario;
import duoc.rocio.inventario.repository.MovimientoInventarioRepository;

@Service
public class MovimientoInventarioService {

    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    public MovimientoInventario registrarMovimiento(ProductoInventario producto, String tipoMovimiento, int cantidad, String motivo, int stockAnterior, int stockPosterior, Long idResponsable) {

        MovimientoInventario movimiento = new MovimientoInventario();

        movimiento.setProducto(producto);
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setCantidad(cantidad);
        movimiento.setMotivo(motivo);
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setStockAnterior(stockAnterior);
        movimiento.setStockPosterior(stockPosterior);
        movimiento.setIdResponsable(idResponsable);

        return movimientoInventarioRepository.save(movimiento);
    }

    // Obtener todos los movimientos
    public List<MovimientoInventario> obtenerMovimientos() {
        return movimientoInventarioRepository.findAllByOrderByFechaDesc();
    }

    // Obtener movimiento por ID
    public Optional<MovimientoInventario> obtenerMovimientoPorId(Long idMovimiento) {
        return movimientoInventarioRepository.findById(idMovimiento);
    }

    // Obtener movimientos de un producto
    public List<MovimientoInventario> obtenerMovimientosPorProducto(Long idProducto) {
        return movimientoInventarioRepository.findByProducto_IdProductoOrderByFechaDesc(idProducto);
    }

    // Obtener movimientos por tipo
    public List<MovimientoInventario> obtenerMovimientosPorTipo(String tipoMovimiento) {
        return movimientoInventarioRepository.findByTipoMovimientoIgnoreCaseOrderByFechaDesc(tipoMovimiento);
    }

    // Obtener movimientos realizados por un empleado
    public List<MovimientoInventario> obtenerMovimientosPorResponsable(Long idResponsable) {
        return movimientoInventarioRepository.findByIdResponsableOrderByFechaDesc(idResponsable);
    }
}