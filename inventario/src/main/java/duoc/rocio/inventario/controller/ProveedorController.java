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
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    // Obtener todos los proveedores
    @GetMapping
    public ResponseEntity<?> obtenerProveedores() {

        List<Proveedor> proveedores = proveedorService.obtenerProveedores();

        if (proveedores.isEmpty()) {
            return ResponseEntity.status(204).body("No existen proveedores registrados");
        }

        return ResponseEntity.status(200).body(proveedores);
    }

    // Obtener proveedor por ID
    @GetMapping("/{idProveedor}")
    public ResponseEntity<?> obtenerProveedorPorId(
            @PathVariable Long idProveedor) {

        Optional<Proveedor> proveedor = proveedorService.obtenerProveedorPorId(idProveedor);

        if (proveedor.isPresent()) {
            return ResponseEntity.status(200).body(proveedor.get());
        }

        return ResponseEntity.status(204).body("Proveedor no encontrado");
    }

    // Buscar proveedor por nombre
    @GetMapping("/buscarNombre")
    public ResponseEntity<?> buscarPorNombre(@RequestParam String nombre) {

        List<Proveedor> proveedores = proveedorService.buscarPorNombre(nombre);

        if (proveedores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(proveedores);
    }

    // Guardar proveedor
    @PostMapping
    public ResponseEntity<String> guardarProveedor(@Valid @RequestBody Proveedor proveedorNuevo) {
        return proveedorService.guardarProveedor(proveedorNuevo);
    }

    // Actualizar proveedor
    @PutMapping("/{idProveedor}")
    public ResponseEntity<String> actualizarProveedor(@PathVariable Long idProveedor, @Valid @RequestBody Proveedor proveedorActualizado) {
        return proveedorService.actualizarProveedor(idProveedor, proveedorActualizado);
    }

    // Eliminar proveedor
    @DeleteMapping("/{idProveedor}")
    public ResponseEntity<String> eliminarProveedor(@PathVariable Long idProveedor) {
        return proveedorService.eliminarProveedor(idProveedor);
    }
}