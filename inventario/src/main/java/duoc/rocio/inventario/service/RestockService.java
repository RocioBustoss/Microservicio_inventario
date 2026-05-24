package duoc.rocio.inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import duoc.rocio.inventario.dto.SolicitudRestockDTO;
import duoc.rocio.inventario.model.ProductoInventario;
import duoc.rocio.inventario.model.Proveedor;
import duoc.rocio.inventario.model.Restock;
import duoc.rocio.inventario.repository.ProductoInventarioRepository;
import duoc.rocio.inventario.repository.ProveedorRepository;
import duoc.rocio.inventario.repository.RestockRepository;

@Service
public class RestockService {

    @Autowired
    private RestockRepository restockRepository;

    @Autowired
    private ProductoInventarioRepository productoInventarioRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    // Obtener solicitudes de restock de un producto
    public List<Restock> obtenerSolicitudesPorProducto(Long idProducto) {
        return restockRepository.findByProducto_IdProducto(idProducto);
    }


    // Obtener una solicitud específica
    public ResponseEntity<?> obtenerSolicitudPorId(Long idRestock) {

        Optional<Restock> solicitud = restockRepository.findById(idRestock);
    
        if (solicitud.isEmpty()) {
            return ResponseEntity.status(404).body("Solicitud de restock no encontrada");
        }
    
        return ResponseEntity.status(200).body(solicitud.get());
    }


    // Crear solicitud de restock
    public ResponseEntity<String> solicitarRestock(Long idProducto, Long idProveedor, SolicitudRestockDTO datosSolicitud) {

        Optional<ProductoInventario> productoEncontrado = productoInventarioRepository.findById(idProducto);

        if (productoEncontrado.isEmpty()) {
            return ResponseEntity.status(404).body("Producto no encontrado");
        }

        Optional<Proveedor> proveedorEncontrado = proveedorRepository.findById(idProveedor);

        if (proveedorEncontrado.isEmpty()) {
            return ResponseEntity.status(404).body("Proveedor no encontrado");
        }

        ProductoInventario producto = productoEncontrado.get();

        if (producto.getStockActual() > producto.getStockMinimo()) {
            return ResponseEntity.status(409).body("El producto aún no se encuentra bajo el stock mínimo");
        }

        Restock nuevaSolicitud = new Restock();

        nuevaSolicitud.setFechaSolicitud(java.time.LocalDate.now());
        nuevaSolicitud.setCantidadSolicitada(datosSolicitud.getCantidadSolicitada());
        nuevaSolicitud.setEstado("PENDIENTE");
        nuevaSolicitud.setIdSolicitante(datosSolicitud.getIdSolicitante());
        nuevaSolicitud.setIdAprobador(null);
        nuevaSolicitud.setProducto(producto);
        nuevaSolicitud.setProveedor(proveedorEncontrado.get());

        restockRepository.save(nuevaSolicitud);

        return ResponseEntity.status(201).body("Solicitud de restock creada correctamente");
    }



    // Revisar solicitud de restock y actualizar valores. (Transactional obliga a que todos los puntos se cumplan para aprobar)
    @Transactional
    public ResponseEntity<String> aprobarRestock(Long idRestock, Long idAprobador) {

        Optional<Restock> solicitudEncontrada = restockRepository.findById(idRestock);

        if (solicitudEncontrada.isEmpty()) {
            return ResponseEntity.status(404).body("Solicitud de restock no encontrada");
        }

        Restock solicitud = solicitudEncontrada.get();

        if (!solicitud.getEstado().equalsIgnoreCase("PENDIENTE")) {
            return ResponseEntity.status(409).body("La solicitud ya fue procesada");
        }

        ProductoInventario producto = solicitud.getProducto();

        int nuevoStock = producto.getStockActual() + solicitud.getCantidadSolicitada();

        producto.setStockActual(nuevoStock);

        if (nuevoStock == 0) {
            producto.setEstadoProd("SIN_STOCK");
        } else if (nuevoStock <= producto.getStockMinimo()) {
            producto.setEstadoProd("STOCK_BAJO");
        } else {
            producto.setEstadoProd("ACTIVO");
        }

        solicitud.setIdAprobador(idAprobador);
        solicitud.setEstado("APROBADO");

        productoInventarioRepository.save(producto);
        restockRepository.save(solicitud);

        return ResponseEntity.status(200).body("Solicitud aprobada y stock actualizado correctamente");
    }


    // Rechazar el restock
    public ResponseEntity<String> rechazarRestock(Long idRestock, Long idAprobador) {

        Optional<Restock> solicitudEncontrada = restockRepository.findById(idRestock);

        if (solicitudEncontrada.isEmpty()) {
            return ResponseEntity.status(404).body("Solicitud de restock no encontrada");
        }

        Restock solicitud = solicitudEncontrada.get();

        if (!solicitud.getEstado().equalsIgnoreCase("PENDIENTE")) {
            return ResponseEntity.status(409).body("La solicitud ya fue procesada");
        }

        solicitud.setIdAprobador(idAprobador);
        solicitud.setEstado("RECHAZADO");

        restockRepository.save(solicitud);

        return ResponseEntity.status(200).body("Solicitud de restock rechazada correctamente");
    }


    //Obtiene todas las solicitudes según el estado
    public List<Restock> buscarPorEstado(String estado) {
        return restockRepository.findByEstadoIgnoreCase(estado);
    }


    //Obtiene todas las solicitudes hechas por un usuario
    public List<Restock> buscarPorSolicitante(Long idSolicitante) {
        return restockRepository.findByIdSolicitante(idSolicitante);
    }
}