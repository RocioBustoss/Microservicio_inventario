package duoc.rocio.inventario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return restockService.crearListaRestock(idProveedor, idSolicitante);
    }

    // Agregar un producto a la lista
    // POST api/ecomarket/v1/restocks/1/productos/5?cant=10
    @PostMapping("/{idRestock}/productos/{idProdInv}")
    public ResponseEntity<String> agregarProd(@PathVariable Long idRestock, @PathVariable Long idProdInv, @RequestParam int cant) {
        return restockService.agregarProd(idRestock, idProdInv, cant);
    }

    // Modificar la cantidad de un producto en la lista
    // PUT api/ecomarket/v1/restocks/1/productos/3?cant=15
    @PutMapping("/{idRestock}/productos/{idProdRestock}")
    public ResponseEntity<String> modificarCant(@PathVariable Long idRestock, @PathVariable Long idProdRestock, @RequestParam int cant) {
        return restockService.modificarCant(idRestock, idProdRestock, cant);
    }

    // Eliminar un producto de la lista
    // DELETE api/ecomarket/v1/restocks/1/productos/3
    @DeleteMapping("/{idRestock}/productos/{idProdRestock}")
    public ResponseEntity<String> eliminarProd(@PathVariable Long idRestock, @PathVariable Long idProdRestock) {
        return restockService.eliminarProd(idRestock, idProdRestock);
    }

    // Obtener una solicitud específica (Con todos sus datos)
    // GET api/ecomarket/v1/restocks/1
    @GetMapping("/{idRestock}")
    public ResponseEntity<?> mostrarRestock(@PathVariable Long idRestock) {
        return restockService.mostrarRestock(idRestock);
    }

    // Obtener solo los productos de una solicitud
    // GET api/ecomarket/v1/restocks/1/productos
    @GetMapping("/{idRestock}/productos")
    public ResponseEntity<?> mostrarProductos(@PathVariable Long idRestock) {
        return restockService.mostrarProductos(idRestock);
    }

    // Aprobar solicitud
    // PUT api/ecomarket/v1/restocks/1/aprobar?idAprobador=7
    @PutMapping("/{idRestock}/aprobar")
    public ResponseEntity<String> aprobarRestock(@PathVariable Long idRestock, @RequestParam Long idAprobador) {
                
        return restockService.aprobarRestock(idRestock, idAprobador);
    }

    // Rechazar solicitud
    // PUT api/ecomarket/v1/restocks/1/rechazar?idAprobador=7
    @PutMapping("/{idRestock}/rechazar")
    public ResponseEntity<String> rechazarRestock(@PathVariable Long idRestock, @RequestParam Long idAprobador) {
        return restockService.rechazarRestock(idRestock, idAprobador);
    }
}