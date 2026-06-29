package duoc.rocio.inventario.controller;

import java.util.ArrayList;
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

    


    //Agregar un producto a un inventario específico
    // POST api/ecomarket/v1/productos/inventario/1
    @PostMapping("/inventario/{idInventario}")
    public ResponseEntity<String> agregarProducto(@PathVariable Long idInventario, @Valid @RequestBody ProductoInventario productoNuevo) {
        return productoInventarioService.agregarProd(idInventario, productoNuevo);
    }
    

    //Obtener todos los productos de un inventario
    // GET api/ecomarket/v1/productos/inventario/1
    @GetMapping("/inventario/{idInventario}")
    public List<ProductoDTO> obtenerProductosPorInventario(@PathVariable Long idInventario) {
        
        List<ProductoInventario> productos = productoInventarioService.getProductosByInv(idInventario);

        List<ProductoDTO> prodDTOs = new ArrayList<>();
        
        for (ProductoInventario prod : productos) {
            prodDTOs.add(new ProductoDTO(
                prod.getIdProducto(), 
                prod.getNombreProd(), 
                prod.getStockActual()
            ));
        }

        return prodDTOs;
    }

    // Obtener un producto específico dentro de un inventario específico
    // GET api/ecomarket/v1/productos/inventario/2/producto/1
    @GetMapping("/inventario/{idInventario}/producto/{idProducto}")
    public ResponseEntity<?> obtenerProductoDeInventario(
            @PathVariable Long idInventario, 
            @PathVariable Long idProducto) {
                
        return productoInventarioService.getProductoByInvAndId(idInventario, idProducto);
    }

    // Buscar producto por nombre dentro de un inventario
    // GET api/ecomarket/v1/productos/inventario/1/buscarNombre?nombre=quix
    @GetMapping("/inventario/{idInventario}/buscarNombre")
    public ResponseEntity<?> buscarPorNombre(@PathVariable Long idInventario, @RequestParam String nombre) {

        List<ProductoInventario> productos = productoInventarioService.findByNombre(idInventario, nombre);

        if (productos.isEmpty()) {
            return ResponseEntity.status(204).body("No existen productos con el nombre solicitado en este inventario");
        }

        return ResponseEntity.status(200).body(productos);
    }


    // Filtra productos con stock bajo umbral y de inventario especifico
    // GET api/ecomarket/v1/productos/inventario/1/stock-bajo/5
    @GetMapping("/inventario/{idInv}/stock-bajo/{umbral}")
    public List<ProductoDTO> filtrarStockBajo(@PathVariable Long idInv, @PathVariable int umbral) {
        List<ProductoInventario> productos = productoInventarioService.buscarStockBajo(idInv, umbral);

        List<ProductoDTO> prodDTOs = new ArrayList<>();
        
        // Conversión ProductoInventario -> ProductoDTO
        for (ProductoInventario prod : productos) {
            prodDTOs.add(new ProductoDTO(
                prod.getIdProducto(), 
                prod.getNombreProd(), 
                prod.getStockActual()));
        }

        return prodDTOs;
    }
    
    //Obtener un producto por su id
    // GET api/ecomarket/v1/productos/1
    @GetMapping("/{idProducto}")
    public ResponseEntity<?> obtenerProductoPorId(@PathVariable Long idProducto) {

        Optional<ProductoInventario> producto = productoInventarioService.getProdById(idProducto);

        if (!producto.isEmpty()) {
            return ResponseEntity.status(200).body(producto.get());
        }
        return ResponseEntity.status(404).body("Producto no encontrado");
    }

    //Obtiene un producto por su id
    // Devuelve true si lo encunetra, false de otro modo
    @GetMapping("/{idProducto}/conexion")
    public boolean existeProductoPorId(@PathVariable Long idProducto) {

        Optional<ProductoInventario> producto = productoInventarioService.getProdById(idProducto);

        if (producto.isPresent()) {
            return true;
        }
        return false;
    }
    
    //Consultar el stock y los datos de un producto en un inventario específico
    // GET api/ecomarket/v1/productos/inventario/1/producto/1/stock
    @GetMapping("/inventario/{idInventario}/producto/{idProducto}/stock")
    public ResponseEntity<?> consultarStock(@PathVariable Long idInventario, @PathVariable Long idProducto) {
        return productoInventarioService.consultarStock(idInventario, idProducto);
    }

    //Actualizar el stock de un producto específico en un inventario específico
    // PUT api/ecomarket/v1/productos/inventario/1/producto/1/stock?cantidad=20
    @PutMapping("/inventario/{idInventario}/producto/{idProducto}/stock")
    public ResponseEntity<String> actualizarStock(@PathVariable Long idInventario, @PathVariable Long idProducto, @RequestParam int cantidad) {
        return productoInventarioService.actualizarStock(idInventario, idProducto, cantidad);
    }

    //Eliminar un producto de un inventario
    // DELETE api/ecomarket/v1/productos/inventario/1/producto/1
    @DeleteMapping("/inventario/{idInventario}/producto/{idProducto}")
    public ResponseEntity<String> eliminarProductoSistema(@PathVariable Long idInventario,@PathVariable Long idProducto) {
        return productoInventarioService.eliminarProducto(idProducto);
    }

    


}