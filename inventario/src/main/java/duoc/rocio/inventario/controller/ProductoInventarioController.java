package duoc.rocio.inventario.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import duoc.rocio.inventario.model.ProductoInventario;
import duoc.rocio.inventario.service.ProductoInventarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/inventario/productos")
public class ProductoInventarioController {

    @Autowired
    private ProductoInventarioService productoInventarioService;

    //Obtener todos los productos
    @GetMapping
    public List<ProductoInventario> obtenerProductos() {
        return productoInventarioService.obtenerProductos();
    }

    //Obtener producto por ID
    @GetMapping("buscarId/{idProducto}")
    public ResponseEntity<ProductoInventario> obtenerProductoPorId(@PathVariable Long idProducto) {
        Optional<ProductoInventario> producto = productoInventarioService.obtenerProductoPorId(idProducto);

        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.get());
        }

        return ResponseEntity.notFound().build();
    }

    //Guardar producto
    @PostMapping
    public ResponseEntity<String> guardarProducto(@Valid @RequestBody ProductoInventario productoNuevo) {
        return productoInventarioService.guardarProducto(productoNuevo);
    }

    //Eliminar producto
    @DeleteMapping("eliminar/{idProducto}")
    public ResponseEntity<String> eliminarProducto(@PathVariable Long idProducto) {
        return productoInventarioService.eliminarProducto(idProducto);
    }

    //Buscar producto por nombre
    @GetMapping("buscarNombre/{nombre}")
    public List<ProductoInventario> buscarPorNombre(@PathVariable String nombre) {
        return productoInventarioService.buscarPorNombre(nombre);
    }

    //Buscar productos por estado
    @GetMapping("buscarEstado/{estado}")
    public List<ProductoInventario> buscarPorEstado(@PathVariable String estado) {
        return productoInventarioService.buscarPorEstado(estado);
    }

    //Buscar productos con stock bajo
    @GetMapping("verificarStock/{umbral}")
    public List<ProductoInventario> buscarStockBajo(@PathVariable int umbral) {
        return productoInventarioService.buscarStockBajo(umbral);
    }
}