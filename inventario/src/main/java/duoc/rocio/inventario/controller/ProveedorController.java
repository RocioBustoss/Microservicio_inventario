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
        int resultado = proveedorService.guardarProv(proveedorNuevo);
        
        if (resultado == 1) {
            return ResponseEntity.status(409).body("El proveedor ya se encuentra registrado");
        }
        
        return ResponseEntity.status(201).body("Proveedor registrado correctamente");
    }

    // Actualizar proveedor
    // PUT api/ecomarket/v1/proveedores/1
    @PutMapping("/{idProveedor}")
    public ResponseEntity<String> actualizarProveedor(@PathVariable Long idProveedor, @Valid @RequestBody Proveedor proveedorActualizado) {
        boolean actualizado = proveedorService.actualizarProveedor(idProveedor, proveedorActualizado);
        
        if (!actualizado) {
            return ResponseEntity.status(404).body("Proveedor no encontrado");
        }
        
        return ResponseEntity.ok("Proveedor actualizado correctamente");
    }

    // Eliminar proveedor
    // DELETE api/ecomarket/v1/proveedores/1
    @DeleteMapping("/{idProveedor}")
public ResponseEntity<String> eliminarProveedor(@PathVariable Long idProveedor) {
        
        boolean eliminado = proveedorService.eliminarProv(idProveedor);
        
        if (eliminado) {
            return ResponseEntity.ok("Proveedor eliminado correctamente");
        } else {
            return ResponseEntity.status(404).body("Proveedor no encontrado");
        }
    }
}