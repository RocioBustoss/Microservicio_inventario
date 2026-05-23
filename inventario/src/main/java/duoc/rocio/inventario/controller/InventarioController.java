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

import duoc.rocio.inventario.model.Inventario;
import duoc.rocio.inventario.service.InventarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventarios")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    // Obtener todos los inventarios existentes
    @GetMapping
    public ResponseEntity<?> obtenerInventarios() {

        List<Inventario> inventarios = inventarioService.obtenerInventarios();

        if (inventarios.isEmpty()) {
            return ResponseEntity.status(204).body("No existen inventarios registrados en el sistema");
        }

        return ResponseEntity.status(200).body(inventarios);
    }


    //Obtener un inventario por su id
    @GetMapping("/{idInventario}")
    public ResponseEntity<?> obtenerInventarioPorId(@PathVariable Long idInventario) {

        Optional<Inventario> inventario = inventarioService.obtenerInventarioPorId(idInventario);

        if (!inventario.isEmpty()) {
            return ResponseEntity.status(200).body(inventario.get());
        }

        return ResponseEntity.status(404).body("Inventario no encontrado");
    }

    // Obtener el inventario de una tienda especifica
    @GetMapping("/tienda/{idTienda}")
    public ResponseEntity<?> obtenerInventarioPorTienda(@PathVariable Long idTienda) {

        List<Inventario> inventarios = inventarioService.obtenerInventarioPorTienda(idTienda);

        if (inventarios.isEmpty()) {
            return ResponseEntity.status(404).body("No se encontró inventario para la tienda");
        }

        return ResponseEntity.status(200).body(inventarios);
    }

    // Crear inventario para una tienda
    @PostMapping
    public ResponseEntity<String> guardarInventario(@Valid @RequestBody Inventario inventarioNuevo) {
        return inventarioService.guardarInventario(inventarioNuevo);
    }

    // Eliminar inventario
    @DeleteMapping("/{idInventario}")
    public ResponseEntity<String> eliminarInventario(@PathVariable Long idInventario) {
        return inventarioService.eliminarInventario(idInventario);
    }
}
