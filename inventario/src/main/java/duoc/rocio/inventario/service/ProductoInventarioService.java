package duoc.rocio.inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import duoc.rocio.inventario.model.ProductoInventario;
import duoc.rocio.inventario.repository.ProductoInventarioRepository;

@Service
public class ProductoInventarioService {
    @Autowired
    private ProductoInventarioRepository productoInventarioRepository;

    //Obtener todos los productos
    public List<ProductoInventario> obtenerProductos(){
        return productoInventarioRepository.findAll();
    }
    
    //Obtener producto por Id
    public Optional<ProductoInventario> obtenerProductoPorId(Long idProducto){
        return productoInventarioRepository.findById(idProducto);
    }

    //Guardar producto
    public ResponseEntity<String> guardarProducto(ProductoInventario productoNuevo){
        if(productoInventarioRepository.existsByCodigoSku(productoNuevo.getCodigoSku())){
            //Si el producto existe, no lo agrego
            return ResponseEntity.status(409).body("Producto ya existe");

        }
        productoInventarioRepository.save(productoNuevo);
        return ResponseEntity.status(200).body("Producto guardado correctamente");
    }

    //Eliminar producto
    public ResponseEntity<String> eliminarProducto(Long idProductoEliminar){
        if(productoInventarioRepository.existsById(idProductoEliminar)){
            productoInventarioRepository.deleteById(idProductoEliminar);
            return ResponseEntity.status(200).body("Producto eliminado correctamente");
        }
        return ResponseEntity.status(404).body("Producto no encontrado");
    }

    //Buscar producto por nombre
    public List<ProductoInventario> buscarPorNombre(String nombreBuscado){
        return productoInventarioRepository.findByNombreProInvContainingIgnoreCase(nombreBuscado);
    }

    //Buscar por estado
    public List<ProductoInventario> buscarPorEstado(String estadoBuscado){
        return productoInventarioRepository.findByEstadoProd(estadoBuscado);
    }

    //Buscar por stock 
    public List<ProductoInventario> buscarStockBajo(int umbral){
        return productoInventarioRepository.findByStockActualLessThanEqual(umbral);
    }


}
