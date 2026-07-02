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

import duoc.rocio.inventario.model.Tienda;
import duoc.rocio.inventario.service.TiendaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ecomarket/v1/tiendas")
public class TiendaController {

    @Autowired
    private TiendaService tiendaService;

    // Obtener todos los inventarios existentes
    // GET api/ecomarket/v1/inventarios
    @GetMapping
    public ResponseEntity<?> obtenerTiendas() {
        List<Tienda> tiendas = tiendaService.getTiendas();

        if (tiendas.isEmpty()) {
            return ResponseEntity.status(204).body("No existen inventarios registrados en el sistema");
        }

        return ResponseEntity.status(200).body(tiendas);
    }


    //Obtener un inventario por su id
    // GET api/ecomarket/v1/inventarios/1
    @GetMapping("/{idTienda}")
    public ResponseEntity<?> obtenerTiePorId(@PathVariable Long idTienda) {
        Optional<Tienda> tienda = tiendaService.getTieById(idTienda);

        if (!tienda.isEmpty()) {
            return ResponseEntity.status(200).body(tienda.get());
        }
        return ResponseEntity.status(404).body("Tienda no encontrada");
    }

    // Crear inventario para una tienda
    // POST api/ecomarket/v1/inventarios
    @PostMapping
    public ResponseEntity<Tienda> guardarTie(@Valid @RequestBody Tienda tieNueva) {
        Tienda tiendaGuardada = tiendaService.guardarTie(tieNueva);
        return ResponseEntity.status(201).body(tiendaGuardada);
    }

    // Eliminar inventario
    // DELETE api/ecomarket/v1/inventarios/1
    @DeleteMapping("/{idTienda}")
    public ResponseEntity<String> eliminarInventario(@PathVariable Long idTienda) {
        tiendaService.eliminarTie(idTienda);
        return ResponseEntity.status(200).body("Tienda eliminada correctamente");
    }
}
