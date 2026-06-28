package duoc.rocio.inventario.service;

import java.util.ArrayList;
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


    //Agraga un producto a un inventario específico (ej: quix al inventario de San Pedro)
    public ResponseEntity<String> agregarProd(Long idInventario, ProductoInventario productoNuevo) {
        Optional<Inventario> inventarioEncontrado = inventarioRepository.findById(idInventario);
        
        if (inventarioEncontrado.isEmpty()) {
            return ResponseEntity.status(404).body("Inventario no encontrado");
        }
        
        if (productoInventarioRepository.existsByInventario_IdInventarioAndCodigoSku(idInventario, productoNuevo.getCodigoSku())) {
            return ResponseEntity.status(409).body("El producto ya existe en este inventario");
        }
        
        productoNuevo.setInventario(inventarioEncontrado.get());
        productoInventarioRepository.save(productoNuevo);
        
        return ResponseEntity.status(201).body("Producto agregado al inventario correctamente");
    }
    
    
    public List<ProductoInventario> getProductosByInv(Long idInventario){
        Optional<Inventario> inventarioEncontrado = inventarioRepository.findById(idInventario);
        
        // Si no existe el inventario, devolvemos una lista vacía
        if (inventarioEncontrado.isEmpty()) {
            return new ArrayList<>(); 
        }

        // Extraemos el inventario y retornamos su lista de productos
        Inventario inventario = inventarioEncontrado.get();
        return inventario.getProductos();        
    }
    
    
    //Obtiene un producto por su id
    public Optional<ProductoInventario> getProdById(Long idProducto) {
        return productoInventarioRepository.findById(idProducto);
    }
    

    // Obtener un producto cruzando su ID y el ID de su inventario
    public ResponseEntity<?> getProductoByInvAndId(Long idInventario, Long idProducto) {
        
        Optional<ProductoInventario> productoOpt = productoInventarioRepository.findByIdProductoAndInventario_IdInventario(idProducto, idInventario);

        if (productoOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Producto no encontrado en el inventario especificado");
        }

        return ResponseEntity.status(200).body(productoOpt.get());
    }

    //Busca el stock de un producto por su id
    public ResponseEntity<?> consultarStock(Long idProducto, Long idInventario) {
        Optional<ProductoInventario> productoEncontrado = productoInventarioRepository.findByIdProductoAndInventario_IdInventario(idProducto, idInventario);

        if (productoEncontrado.isEmpty()) {
            return ResponseEntity.status(404).body("Producto no encontrado");
        }

        ProductoInventario producto = productoEncontrado.get();
        Integer stockActual = producto.getStockActual();

        return ResponseEntity.status(200).body(stockActual);

    }

    
    // Actualiza el stock por su id y asigna una cantidad.
    @Transactional
    public ResponseEntity<String> actualizarStock(Long idInventario, Long idProducto, int cantidad) {
        
        
        Optional<ProductoInventario> productoEncontrado = productoInventarioRepository.findByIdProductoAndInventario_IdInventario(idProducto, idInventario);
        
        if (productoEncontrado.isEmpty()) {
            return ResponseEntity.status(404).body("Producto no encontrado");
        }
        
        if (cantidad < 0) {
            return ResponseEntity.status(400).body("El stock a sumar no puede ser menor que cero");
        }
        
        ProductoInventario producto = productoEncontrado.get();
        
        Integer stockActual = producto.getStockActual();
        Integer stockPosterior = stockActual + cantidad;
        
        if (stockActual.equals(stockPosterior)) {
            return ResponseEntity.status(409).body("El stock ingresado es igual al stock actual");
        }
        
        producto.setStockActual(stockPosterior);
        productoInventarioRepository.save(producto);
        
        return ResponseEntity.status(200).body("Stock actualizado corectamente");
    }
    
    
    // busca un producto en un inventario por su nombre
    public List<ProductoInventario> findByNombre(Long idInventario, String nombreBuscado) {
        return productoInventarioRepository.findByInventario_IdInventarioAndNombreProdContainingIgnoreCase(idInventario, nombreBuscado);
    }
    
    
    // busca productos con stock menor a un umbral
    public List<ProductoInventario> buscarStockBajo(Long idInventario,int umbral) {
        return productoInventarioRepository.findByInventario_IdInventarioAndStockActualLessThanEqual(idInventario, umbral);
    }

    //Elimina un producto por su id
    public ResponseEntity<String> eliminarProducto(Long idProducto) {
        if (!productoInventarioRepository.existsById(idProducto)) {
            return ResponseEntity.status(404).body("Producto no encontrado");
        }
    
        productoInventarioRepository.deleteById(idProducto);
        return ResponseEntity.status(200).body("Producto eliminado correctamente");
    }

}
