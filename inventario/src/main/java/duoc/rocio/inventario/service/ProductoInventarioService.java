package duoc.rocio.inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import duoc.rocio.inventario.model.Inventario;
import duoc.rocio.inventario.model.ProductoInventario;
import duoc.rocio.inventario.repository.InventarioRepository;
import duoc.rocio.inventario.repository.ProductoInventarioRepository;
import jakarta.transaction.Transactional;


@Service
public class ProductoInventarioService {

    //Conecta al repositorio de productoInventario
    @Autowired
    private ProductoInventarioRepository productoInventarioRepository;

    //Conecta al repositorio de inventario
    @Autowired
    private InventarioRepository inventarioRepository;

    //Conectar al repositorio de movimiento de inventario
    @Autowired
    private MovimientoInventarioService movimientoInventarioService;

    //Obtiene todos los productos de un inventario
    public List<ProductoInventario> obtenerProductosPorInventario(Long idInventario) {
        return productoInventarioRepository.findByInventarioIdInventario(idInventario);
    }

    //Obtiene un producto por su id
    public Optional<ProductoInventario> obtenerProductoPorId(Long idProducto) {
        return productoInventarioRepository.findById(idProducto);
    }

    //Agraga un producto a un inventario específico (ej: quix a la tienda de San Pedro)
    public ResponseEntity<String> agregarProducto(Long idInventario, ProductoInventario productoNuevo) {
        Optional<Inventario> inventarioEncontrado = inventarioRepository.findById(idInventario);

        if (inventarioEncontrado.isEmpty()) {
            return ResponseEntity.status(404).body("Inventario no encontrado");
        }

        if (productoInventarioRepository.existsByInventarioIdInventarioAndCodigoSku(idInventario, productoNuevo.getCodigoSku())) {
            return ResponseEntity.status(409).body("El producto ya existe en este inventario");
        }

        productoNuevo.setInventario(inventarioEncontrado.get());
        productoInventarioRepository.save(productoNuevo);

        return ResponseEntity.status(201).body("Producto agregado al inventario correctamente");
    }

    //Elimina un producto por su id
    public ResponseEntity<String> eliminarProducto(Long idProducto) {
        if (!productoInventarioRepository.existsById(idProducto)) {
            return ResponseEntity.status(404).body("Producto no encontrado");
        }

        productoInventarioRepository.deleteById(idProducto);
        return ResponseEntity.status(200).body("Producto eliminado correctamente");
    }

    //Busca el stock de un producto por su id
    public ResponseEntity<?> consultarStock(Long idProducto, Long idInventario) {
        Optional<ProductoInventario> productoEncontrado = productoInventarioRepository.findByIdProductoAndInventarioIdInventario(idProducto, idInventario);

        if (productoEncontrado.isEmpty()) {
            return ResponseEntity.status(404).body("Producto no encontrado");
        }

        return ResponseEntity.status(200).body(productoEncontrado.get());
    }

    // Actualiza el stock por su id y asigna una cantidad. Luego agrega a los movimientos de inventario
    @Transactional
    public ResponseEntity<String> actualizarStock(Long idInventario, Long idProducto, int cantidad, Long idResponsable, String motivo) {

        if (cantidad < 0) {
            return ResponseEntity.status(400).body("El stock no puede ser menor que cero");
        }

        Optional<ProductoInventario> productoEncontrado = productoInventarioRepository.findByIdProductoAndInventario_IdInventario(idInventario, idProducto);

        if (productoEncontrado.isEmpty()) {
            return ResponseEntity.status(404).body("Producto no encontrado");
        }

        ProductoInventario producto = productoEncontrado.get();

        int stockAnterior = producto.getStockActual();
        int stockPosterior = cantidad;

        if (stockAnterior == stockPosterior) {
            return ResponseEntity.status(409).body("El stock ingresado es igual al stock actual");
        }

        String tipoMovimiento;
        int cantidadMovimiento;

        if (stockPosterior > stockAnterior) {
            tipoMovimiento = "AJUSTE_ENTRADA";
            cantidadMovimiento = stockPosterior - stockAnterior;
        } else {
            tipoMovimiento = "AJUSTE_SALIDA";
            cantidadMovimiento = stockAnterior - stockPosterior;
        }

        producto.setStockActual(stockPosterior);

        if (stockPosterior == 0) {
            producto.setEstadoProd("SIN_STOCK");
        } else if (stockPosterior <= producto.getStockMinimo()) {
            producto.setEstadoProd("STOCK_BAJO");
        } else {
            producto.setEstadoProd("ACTIVO");
        }

        productoInventarioRepository.save(producto);

        movimientoInventarioService.registrarMovimiento(producto, tipoMovimiento, cantidadMovimiento, motivo, stockAnterior, stockPosterior, idResponsable);

        return ResponseEntity.status(200).body("Stock actualizado y movimiento registrado correctamente");
    }

    // busca un producto en un inventario por su nombre
    public List<ProductoInventario> buscarPorNombre(Long idInventario, String nombreBuscado) {
        return productoInventarioRepository.findByInventarioIdInventarioAndNombreProInvContainingIgnoreCase(idInventario, nombreBuscado);
    }

    // busca un producto en un inventario por su estado
    public List<ProductoInventario> buscarPorEstado(Long idInventario, String estadoBuscado) {
        return productoInventarioRepository.findByInventarioIdInventarioAndEstadoProdContainingIgnoreCase( idInventario, estadoBuscado);
    }

    // busca productos con stock menor a un umbral
    public List<ProductoInventario> buscarStockBajo(Long idInventario,int umbral) {
        return productoInventarioRepository.findByInventarioIdInventarioAndStockActualLessThanEqual(idInventario, umbral);
    }
}
