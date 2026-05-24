package duoc.rocio.inventario.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import duoc.rocio.inventario.model.MovimientoInventario;
import duoc.rocio.inventario.service.MovimientoInventarioService;

@RestController
@RequestMapping("/api/ecomarket/v1/movimientos")
public class MovimientoInventarioController {

    @Autowired
    private MovimientoInventarioService movimientoInventarioService;

    //Obtener todos los movimientos
    @GetMapping
    public ResponseEntity<?> obtenerMovimientos() {
        List<MovimientoInventario> movimientos = movimientoInventarioService.obtenerMovimientos();

        if (movimientos.isEmpty()) {
            return ResponseEntity.status(200).body("No existen movimientos registrados en el inventario");
        }

        return ResponseEntity.status(200).body(movimientos);
    }

    //Obtener movimiento por id
    @GetMapping("/{idMovimiento}")
    public ResponseEntity<?> obtenerMovimientoPorId(@PathVariable Long idMovimiento) {
        Optional<MovimientoInventario> movimiento = movimientoInventarioService.obtenerMovimientoPorId(idMovimiento);

        if (movimiento.isEmpty()) {
            return ResponseEntity.status(404).body("Movimiento no encontrado");
        }

        return ResponseEntity.ok(movimiento.get());
    }

    //Obtener movimientos de un producto
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<?> obtenerMovimientosPorProducto(@PathVariable Long idProducto) {
        List<MovimientoInventario> movimientos = movimientoInventarioService.obtenerMovimientosPorProducto(idProducto);

        if (movimientos.isEmpty()) {
            return ResponseEntity.status(200).body("No existen movimientos para el producto seleccionado");
        }

        return ResponseEntity.status(200).body(movimientos);
    }

    //Obtener movimientos por tipo
    @GetMapping("/tipo")
    public ResponseEntity<?> obtenerMovimientosPorTipo(@RequestParam String tipo) {
        List<MovimientoInventario> movimientos = movimientoInventarioService.obtenerMovimientosPorTipo(tipo);

        if (movimientos.isEmpty()) {
            return ResponseEntity.status(200).body("No existen movimientos del tipo solicitado");
        }

        return ResponseEntity.status(200).body(movimientos);
    }

    //Obtener movimientos por responsable
    @GetMapping("/responsable/{idResponsable}")
    public ResponseEntity<?> obtenerMovimientosPorResponsable(@PathVariable Long idResponsable) {
        List<MovimientoInventario> movimientos = movimientoInventarioService.obtenerMovimientosPorResponsable(idResponsable);

        if (movimientos.isEmpty()) {
            return ResponseEntity.status(200).body("No existen movimientos asociados al responsable indicado");
        }

        return ResponseEntity.status(200).body(movimientos);
    }
}