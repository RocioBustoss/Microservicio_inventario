package duoc.rocio.inventario.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import duoc.rocio.inventario.dto.ProductoDTO;
import duoc.rocio.inventario.model.ProductoInventario;
import duoc.rocio.inventario.service.ProductoInventarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ecomarket/v1/productos")
public class ProductoInventarioController {

    @Autowired
    private ProductoInventarioService productoInventarioService;


    //Obtener todos los productos de un inventario
    // GET api/ecomarket/v1/productos/inventario/1
    @GetMapping("/inventario/{idInventario}")
    public ResponseEntity<?> obtenerProductosPorInventario(@PathVariable Long idInventario) {

        List<ProductoInventario> productos = productoInventarioService.obtenerProductosPorInventario(idInventario);

        if (productos.isEmpty()) {
            return ResponseEntity.status(204).body("El inventario para la tienda seleccionada está vacío");
        }
        return ResponseEntity.status(200).body(productos);
    }

    //Agregar un producto a un inventario específico
    // POST api/ecomarket/v1/productos/inventario/1
    @PostMapping("/inventario/{idInventario}")
    public ResponseEntity<String> agregarProducto(@PathVariable Long idInventario, @Valid @RequestBody ProductoInventario productoNuevo) {
        return productoInventarioService.agregarProducto(idInventario, productoNuevo);
    }
    

    // Buscar producto por nombre dentro de un inventario
    // GET api/ecomarket/v1/productos/inventario/1/buscarNombre?nombre=quix
    @GetMapping("/inventario/{idInventario}/buscarNombre")
    public ResponseEntity<?> buscarPorNombre(@PathVariable Long idInventario, @RequestParam String nombre) {

        List<ProductoInventario> productos = productoInventarioService.buscarPorNombre(idInventario, nombre);

        if (productos.isEmpty()) {
            return ResponseEntity.status(204).body("No existen productos con el nombre solicitado en este inventario");
        }

        return ResponseEntity.status(200).body(productos);
    }


    // Buscar productos por estado dentro de un inventario
    // GET api/ecomarket/v1/productos/inventario/1/buscarEstado?estado=ACTIVO
    @GetMapping("/inventario/{idInventario}/buscarEstado")
    public ResponseEntity<?> buscarPorEstado(@PathVariable Long idInventario, @RequestParam String estado) {
        List<ProductoInventario> productos = productoInventarioService.buscarPorEstado(idInventario, estado);

        if (productos.isEmpty()) {
            return ResponseEntity.status(204).body("No existen productos con el estado solicitado en este inventario");
        }
        return ResponseEntity.status(200).body(productos);
    }


    // Busca productos bajo un umbral en un inventario específico
    // GET api/ecomarket/v1/productos/inventario/1/stock-bajo/5
    @GetMapping("/inventario/{idInventario}/stock-bajo/{umbral}")
    public ResponseEntity<?> buscarStockBajo(@PathVariable Long idInventario, @PathVariable int umbral) {
        List<ProductoDTO> productos = productoInventarioService.buscarStockBajo(idInventario, umbral);

        if (productos.isEmpty()) {
            return ResponseEntity.status(204).body("No existen productos con stock menor al umbral establecido");
        }
        return ResponseEntity.status(200).body(productos);
    }
    

    
    
    //Obtener un producto por su id
    // GET api/ecomarket/v1/productos/1
    @GetMapping("/{idProducto}")
    public ResponseEntity<?> obtenerProductoPorId(@PathVariable Long idProducto) {

        Optional<ProductoInventario> producto = productoInventarioService.obtenerProductoPorId(idProducto);

        if (!producto.isEmpty()) {
            return ResponseEntity.status(200).body(producto.get());
        }
        return ResponseEntity.status(404).body("Producto no encontrado");
    }
    
    //Consultar el stock y los datos de un producto en un inventario específico
    // GET api/ecomarket/v1/productos/inventario/1/producto/1/stock
    @GetMapping("/inventario/{idInventario}/producto/{idProducto}/stock")
    public ResponseEntity<?> consultarStock(@PathVariable Long idInventario, @PathVariable Long idProducto) {
        return productoInventarioService.consultarStock(idInventario, idProducto);
    }

    //Actualizar el stock de un producto específico en un inventario específico
    // PUT api/ecomarket/v1/productos/inventario/1/producto/1/stock?cantidad=20&idResponsable=7&motivo=Ajuste%20manual
    @PutMapping("/inventario/{idInventario}/producto/{idProducto}/stock")
    public ResponseEntity<String> actualizarStock(@PathVariable Long idInventario, @PathVariable Long idProducto, @RequestParam int cantidad, @RequestParam Long idResponsable, @RequestParam String motivo) {
        return productoInventarioService.actualizarStock(idInventario, idProducto, cantidad, idResponsable, motivo);
    }

    //Eliminar un producto de un inventario
    // DELETE api/ecomarket/v1/productos/inventario/1/producto/1
    @DeleteMapping("/inventario/{idInventario}/producto/{idProducto}")
    public ResponseEntity<String> eliminarProductoSistema(@PathVariable Long idInventario,@PathVariable Long idProducto) {
        return productoInventarioService.eliminarProducto(idProducto);
    }

    


}