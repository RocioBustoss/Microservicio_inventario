package duoc.rocio.inventario.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duoc.rocio.inventario.dto.InventarioResumenDTO;
import duoc.rocio.inventario.model.Inventario;
import duoc.rocio.inventario.service.InventarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ecomarket/v1/inventarios")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    // Obtener todos los inventarios existentes
    // GET api/ecomarket/v1/inventarios
    @GetMapping
    public ResponseEntity<?> obtenerInventarios() {
        List<InventarioResumenDTO> inventarios = inventarioService.getInventarios();

        if (inventarios.isEmpty()) {
            return ResponseEntity.status(204).body("No existen inventarios registrados en el sistema");
        }

        return ResponseEntity.status(200).body(inventarios);
    }


    //Obtener un inventario por su id
    // GET api/ecomarket/v1/inventarios/1
    @GetMapping("/{idInventario}")
    public ResponseEntity<?> obtenerInventarioPorId(@PathVariable Long idInventario) {
        Optional<Inventario> inventario = inventarioService.getInvById(idInventario);

        if (!inventario.isEmpty()) {
            return ResponseEntity.status(200).body(inventario.get());
        }
        return ResponseEntity.status(404).body("Inventario no encontrado");
    }

    // Crear inventario para una tienda
    // POST api/ecomarket/v1/inventarios
    @PostMapping
    public ResponseEntity<Inventario> guardarInventario(@Valid @RequestBody Inventario inventarioNuevo) {
        Inventario inventarioGuardado = inventarioService.guardarInv(inventarioNuevo);
        return ResponseEntity.status(201).body(inventarioGuardado);
    }

    // Eliminar inventario
    // DELETE api/ecomarket/v1/inventarios/1
    @DeleteMapping("/{idInventario}")
    public ResponseEntity<String> eliminarInventario(@PathVariable Long idInventario) {
        inventarioService.eliminarInv(idInventario);
        return ResponseEntity.status(200).body("Inventario eliminado correctamente");
    }
}
