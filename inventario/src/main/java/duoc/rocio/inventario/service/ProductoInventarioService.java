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
    public int agregarProd(Long idInventario, ProductoInventario productoNuevo) {
        Optional<Inventario> inventarioEncontrado = inventarioRepository.findById(idInventario);
        
        if (inventarioEncontrado.isEmpty()) {
            return 1;
        }
        
        if (productoInventarioRepository.existsByInventario_IdInventarioAndCodigoSku(idInventario, productoNuevo.getCodigoSku())) {
            return 2;
        }
        
        productoNuevo.setInventario(inventarioEncontrado.get());
        productoInventarioRepository.save(productoNuevo);
        
        return 0;
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
    public Optional<ProductoInventario> getProductoByInvAndId(Long idInventario, Long idProducto) {
        return productoInventarioRepository.findByIdProductoAndInventario_IdInventario(idProducto, idInventario);
    }

    //Busca el stock de un producto por su id
    public Optional<Integer> consultarStock(Long idProducto, Long idInventario) {
        Optional<ProductoInventario> productoEncontrado = productoInventarioRepository.findByIdProductoAndInventario_IdInventario(idProducto, idInventario);

        if (productoEncontrado.isPresent()) {
            return Optional.of(productoEncontrado.get().getStockActual());
        }

        return Optional.empty();
    }

    
    // Actualiza el stock por su id y asigna una cantidad.
    @Transactional
    public Integer actualizarStock(Long idInventario, Long idProducto, int cantidad) {
        
        Optional<ProductoInventario> productoEncontrado = productoInventarioRepository.findByIdProductoAndInventario_IdInventario(idProducto, idInventario);
        
        if (productoEncontrado.isEmpty()) return 1;
        
        if (cantidad < 0) return 2;
            
        ProductoInventario producto = productoEncontrado.get();
        
        Integer stockActual = producto.getStockActual();
        Integer stockPosterior = stockActual + cantidad;
        
        if (stockActual.equals(stockPosterior)) return 3;

        producto.setStockActual(stockPosterior);
        productoInventarioRepository.save(producto);
        
        return 0;
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
public boolean eliminarProducto(Long idProducto) {
        if (!productoInventarioRepository.existsById(idProducto)) {
            return false;
        }
    
        productoInventarioRepository.deleteById(idProducto);
        return true;
    }

}
