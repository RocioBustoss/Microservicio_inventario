package duoc.rocio.inventario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import duoc.rocio.inventario.dto.SolicitudRestockDTO;
import duoc.rocio.inventario.model.Restock;
import duoc.rocio.inventario.service.RestockService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ecomarket/v1/restocks")
public class RestockController {

    @Autowired
    private RestockService restockService;

    // Obtener solicitudes de un producto
    // GET api/ecomarket/v1/restocks/producto/1
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<?> obtenerSolicitudesPorProducto(@PathVariable Long idProducto) {

        List<Restock> solicitudes = restockService.obtenerSolicitudesPorProducto(idProducto);

        if (solicitudes.isEmpty()) {
            return ResponseEntity.status(200).body("No existen solicitudes de restock para este producto");
        }

        return ResponseEntity.ok(solicitudes);
    }

    // Obtener una solicitud específica
    // GET api/ecomarket/v1/restocks/1
    @GetMapping("/{idRestock}")
    public ResponseEntity<?> obtenerSolicitudPorId(@PathVariable Long idRestock) {

        return restockService.obtenerSolicitudPorId(idRestock);
    }

    // Crear una solicitud de restock
    // POST api/ecomarket/v1/restocks/producto/1/proveedor/1
    @PostMapping("/producto/{idProducto}/proveedor/{idProveedor}")
    public ResponseEntity<String> solicitarRestock(@PathVariable Long idProducto, @PathVariable Long idProveedor, @Valid @RequestBody SolicitudRestockDTO datosSolicitud) {

        return restockService.solicitarRestock(idProducto, idProveedor, datosSolicitud);
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