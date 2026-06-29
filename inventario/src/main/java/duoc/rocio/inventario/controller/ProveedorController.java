package duoc.rocio.inventario.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import duoc.rocio.inventario.model.Proveedor;
import duoc.rocio.inventario.service.ProveedorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ecomarket/v1/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    // Obtener todos los proveedores
    // GET api/ecomarket/v1/proveedores
    @GetMapping
    public ResponseEntity<?> obtenerProveedores() {

        List<Proveedor> proveedores = proveedorService.getProveedores();

        if (proveedores.isEmpty()) {
            return ResponseEntity.status(204).body("No existen proveedores registrados");
        }

        return ResponseEntity.status(200).body(proveedores);
    }

    // Obtener proveedor por ID
    // GET api/ecomarket/v1/proveedores/1
    @GetMapping("/{idProveedor}")
    public ResponseEntity<?> obtenerProveedorPorId(
            @PathVariable Long idProveedor) {

        Optional<Proveedor> proveedor = proveedorService.getProvById(idProveedor);

        if (proveedor.isPresent()) {
            return ResponseEntity.status(200).body(proveedor.get());
        }

        return ResponseEntity.status(204).body("Proveedor no encontrado");
    }


    // Guardar proveedor
    // POST api/ecomarket/v1/proveedores
    @PostMapping
    public ResponseEntity<String> guardarProveedor(@Valid @RequestBody Proveedor proveedorNuevo) {
        return proveedorService.guardarProv(proveedorNuevo);
    }

    // Actualizar proveedor
    // PUT api/ecomarket/v1/proveedores/1
    @PutMapping("/{idProveedor}")
    public ResponseEntity<String> actualizarProveedor(@PathVariable Long idProveedor, @Valid @RequestBody Proveedor proveedorActualizado) {
        return proveedorService.actualizarProveedor(idProveedor, proveedorActualizado);
    }

    // Eliminar proveedor
    // DELETE api/ecomarket/v1/proveedores/1
    @DeleteMapping("/{idProveedor}")
    public ResponseEntity<String> eliminarProveedor(@PathVariable Long idProveedor) {
        return proveedorService.eliminarProv(idProveedor);
    }
}