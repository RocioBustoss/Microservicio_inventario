package duoc.rocio.inventario.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import duoc.rocio.inventario.model.ProdRestock;
import duoc.rocio.inventario.model.Restock;
import duoc.rocio.inventario.service.RestockService;

@RestController
@RequestMapping("/api/ecomarket/v1/restocks")
public class RestockController {

    @Autowired
    private RestockService restockService;

    // Crear una lista de restock vacía
    // POST api/ecomarket/v1/restocks/proveedor/1/solicitante/2
    @PostMapping("/proveedor/{idProveedor}/solicitante/{idSolicitante}")
    public ResponseEntity<String> crearListaRestock(@PathVariable Long idProveedor, @PathVariable Long idSolicitante) {
        boolean creado = restockService.crearListaRestock(idProveedor, idSolicitante);

        if (!creado){
            return ResponseEntity.status(400).body("Proveedor no encontrado");            
        }
        
        return ResponseEntity.status(201).body("Lista de restock creada.");    
    }

    // Agregar un producto a la lista
    // POST api/ecomarket/v1/restocks/1/productos/5?cant=10
    @PostMapping("/{idRestock}/productos/{idProdInv}")
    public ResponseEntity<String> agregarProd(@PathVariable Long idRestock, @PathVariable Long idProdInv, @RequestParam int cant) {
        Integer productoInteger = restockService.agregarProd(idRestock, idProdInv, cant);

        if (productoInteger == 1){
            return ResponseEntity.status(404).body("Lista de restock no encontrada");
        }
        else if (productoInteger == 2){
            return ResponseEntity.status(404).body("Producto no encontrado en inventario");
        }
        else {
            return ResponseEntity.status(200).body("Producto agregado a la lista de restock");
        }
    }

    // Modificar la cantidad de un producto en la lista
    // PUT api/ecomarket/v1/restocks/1/productos/3?cant=15
    @PutMapping("/{idRestock}/productos/{idProdRestock}")
    public ResponseEntity<String> modificarCant(@PathVariable Long idRestock, @PathVariable Long idProdRestock, @RequestParam int cant) {
        Integer modificaInteger = restockService.modificarCant(idRestock, idProdRestock, cant);

        if (modificaInteger == 1){
            return ResponseEntity.status(404).body("Lista no encontrada");
        }
        else if (modificaInteger == 2){
            return ResponseEntity.status(404).body("El producto no está en esta lista");
        }
        else {
            return ResponseEntity.status(200).body("Cantidad modificada correctamente");
        }

    }

    // Eliminar un producto de la lista
    // DELETE api/ecomarket/v1/restocks/1/productos/3
    @DeleteMapping("/{idRestock}/productos/{idProdRestock}")
    public ResponseEntity<String> eliminarProd(@PathVariable Long idRestock, @PathVariable Long idProdRestock) {
        Integer eliminaInteger = restockService.eliminarProd(idRestock, idProdRestock);

        if (eliminaInteger == 1){
            return ResponseEntity.status(404).body("Lista no encontrada");
        }
        else if (eliminaInteger == 2){
            return ResponseEntity.status(404).body("El producto no se encontró en la lista");
        }
        else {
            return ResponseEntity.status(200).body("Producto removido de la lista");
        }    
    }

    // Obtener una solicitud específica (Con todos sus datos)
    // GET api/ecomarket/v1/restocks/1
    @GetMapping("/{idRestock}")
    public ResponseEntity<?> mostrarRestock(@PathVariable Long idRestock) {
        Optional<Restock> solicitud = restockService.mostrarRestock(idRestock);
        
        if (solicitud.isPresent()) {
            return ResponseEntity.status(200).body(solicitud.get());
        } else {
            return ResponseEntity.status(404).body("Lista no encontrada");
        }
    }

    // Obtener solo los productos de una solicitud
    // GET api/ecomarket/v1/restocks/1/productos
    @GetMapping("/{idRestock}/productos")
    public ResponseEntity<?> mostrarProductos(@PathVariable Long idRestock) {
        Optional<List<ProdRestock>> productos = restockService.mostrarProductos(idRestock);
        
        if (productos.isPresent()) {
            return ResponseEntity.status(200).body(productos.get());
        } else {
            return ResponseEntity.status(404).body("Lista no encontrada");
        }
    }

    // Aprobar solicitud
    // PUT api/ecomarket/v1/restocks/1/aprobar?idAprobador=7
    @PutMapping("/{idRestock}/aprobar")
    public ResponseEntity<String> aprobarRestock(@PathVariable Long idRestock, @RequestParam Long idAprobador) {
        int resultado = restockService.aprobarRestock(idRestock, idAprobador);

        if (resultado == 1) {
            return ResponseEntity.status(404).body("Lista no encontrada");
        }
        else if (resultado == 2){
            return ResponseEntity.status(409).body("La solicitud ya fue procesada");
        }
        else {
            return ResponseEntity.ok("Solicitud aprobada y stock actualizado correctamente");
        }
    }

    // Rechazar solicitud
    // PUT api/ecomarket/v1/restocks/1/rechazar?idAprobador=7
    @PutMapping("/{idRestock}/rechazar")
    public ResponseEntity<String> rechazarRestock(@PathVariable Long idRestock, @RequestParam Long idAprobador) {
        int resultado = restockService.rechazarRestock(idRestock, idAprobador);

        if (resultado == 1) {
            return ResponseEntity.status(404).body("Lista no encontrada");
        }
        else if (resultado == 2){
            return ResponseEntity.status(409).body("La solicitud ya fue procesada");
        }
        else {
            return ResponseEntity.ok("Solicitud de restock rechazada correctamente");
        }    }
}