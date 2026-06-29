package duoc.rocio.inventario.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import duoc.rocio.inventario.dto.SolicitudRestockDTO;
import duoc.rocio.inventario.model.ProdRestock;
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



    // CREAR LISTA DE RESTOCK VACÍA
    public ResponseEntity<String> crearListaRestock(Long idProveedor, Long idSolicitante) {
        Optional<Proveedor> proveedorEncontrado = proveedorRepository.findById(idProveedor);
        
        if (proveedorEncontrado.isEmpty()) {
            return ResponseEntity.status(400).body("Proveedor no encontrado");
        }

        Restock nuevaLista = new Restock();
        nuevaLista.setProveedor(proveedorEncontrado.get());
        nuevaLista.setIdSolicitante(idSolicitante);
        nuevaLista.setFechaSolicitud(LocalDate.now());
        nuevaLista.setEstado("PENDIENTE");

        restockRepository.save(nuevaLista);

        return ResponseEntity.status(201).body("Lista de restock creada.");
    }

    // AGREGAR PRODUCTO A LA LISTA
    @Transactional
    public ResponseEntity<String> agregarProd(Long idRestock, Long idProdInv, int cant) {
        Optional<Restock> restockBuscado = restockRepository.findById(idRestock);
        if (restockBuscado.isEmpty()) return ResponseEntity.status(404).body("Lista de restock no encontrada");

        Optional<ProductoInventario> productoBuscado = productoInventarioRepository.findById(idProdInv);
        if (productoBuscado.isEmpty()) return ResponseEntity.status(404).body("Producto no encontrado en inventario");

        Restock restock = restockBuscado.get();
        ProductoInventario producto = productoBuscado.get();

        // Extraemos el nombre y guardamos la cantidad
        ProdRestock nuevoProdRestock = new ProdRestock();
        nuevoProdRestock.setNombre(producto.getNombreProd()); 
        nuevoProdRestock.setCantidad(cant);

        
        restock.getProductosRestock().add(nuevoProdRestock);
        restockRepository.save(restock);

        return ResponseEntity.status(200).body("Producto agregado a la lista de restock");
    }

    // MODIFICAR CANTIDAD DE UN PRODUCTO EN LA LISTA
    @Transactional
    public ResponseEntity<String> modificarCant(Long idRestock, Long idProdRestock, int nuevaCant) {
        
        Optional<Restock> restockBuscado = restockRepository.findById(idRestock);
        if (restockBuscado.isEmpty()) {
            return ResponseEntity.status(404).body("Lista no encontrada");
        }
        
        Restock restock = restockBuscado.get();
        
        ProdRestock productoEncontrado = null;

        for (ProdRestock p : restock.getProductosRestock()) {
            if (p.getIdProdRestock().equals(idProdRestock)) {
                productoEncontrado = p;
                break;
            }
        }

        if (productoEncontrado == null) {
            return ResponseEntity.status(404).body("El producto no está en esta lista");
        }

        productoEncontrado.setCantidad(nuevaCant);
        restockRepository.save(restock);

        return ResponseEntity.status(200).body("Cantidad modificada correctamente");
    }

    @Transactional
    public ResponseEntity<String> eliminarProd(Long idRestock, Long idProdRestock) {
        
        Optional<Restock> restockBuscado = restockRepository.findById(idRestock);
        if (restockBuscado.isEmpty()) {
            return ResponseEntity.status(404).body("Lista no encontrada");
        }

        Restock restock = restockBuscado.get();
        
        ProdRestock productoAEliminar = null;

        for (ProdRestock p : restock.getProductosRestock()) {
            if (p.getIdProdRestock().equals(idProdRestock)) {
                productoAEliminar = p;
                break;
            }
        }
        if (productoAEliminar == null) {
            return ResponseEntity.status(404).body("El producto no se encontró en la lista");
        }

        restock.getProductosRestock().remove(productoAEliminar);
        restockRepository.save(restock);
        
        return ResponseEntity.status(200).body("Producto removido de la lista");
    }

    // 5. MOSTRAR RESTOCK COMPLETO
    public ResponseEntity<?> mostrarRestock(Long idRestock) {
        Optional<Restock> solicitud = restockRepository.findById(idRestock);
        if (solicitud.isEmpty()) return ResponseEntity.status(404).body("Lista no encontrada");
        
        return ResponseEntity.status(200).body(solicitud.get());
    }

    // 6. MOSTRAR SOLO LOS PRODUCTOS DE UNA LISTA
    public ResponseEntity<?> mostrarProductos(Long idRestock) {
        Optional<Restock> solicitud = restockRepository.findById(idRestock);
        if (solicitud.isEmpty()) return ResponseEntity.status(404).body("Lista no encontrada");
        
        return ResponseEntity.status(200).body(solicitud.get().getProductosRestock());
    }

    // 7. APROBAR RESTOCK
    @Transactional
    public ResponseEntity<String> aprobarRestock(Long idRestock, Long idAprobador) {
        Optional<Restock> solicitudOpt = restockRepository.findById(idRestock);
        if (solicitudOpt.isEmpty()) return ResponseEntity.status(404).body("Lista no encontrada");

        Restock solicitud = solicitudOpt.get();

        if (!solicitud.getEstado().equalsIgnoreCase("PENDIENTE")) {
            return ResponseEntity.status(409).body("La solicitud ya fue procesada");
        }

        // Recorremos todos los productos de la lista para actualizar el inventario real
        for (ProdRestock prodSolicitado : solicitud.getProductosRestock()) {
            
            // Buscamos el producto en el inventario por su NOMBRE
            Optional<ProductoInventario> prodInvOpt = productoInventarioRepository.findByNombreProd(prodSolicitado.getNombre());
            
            if (prodInvOpt.isPresent()) {
                ProductoInventario producto = prodInvOpt.get();
                int stockAnterior = producto.getStockActual();
                int cantidadRecibida = prodSolicitado.getCantidad();
                int nuevoStock = stockAnterior + cantidadRecibida;

                producto.setStockActual(nuevoStock);
                productoInventarioRepository.save(producto);

            }
        }

        solicitud.setIdAprobador(idAprobador);
        solicitud.setEstado("APROBADO");
        restockRepository.save(solicitud);

        return ResponseEntity.status(200).body("Solicitud aprobada y stock actualizado para todos los productos de la lista");
    }

    // 8. RECHAZAR RESTOCK
    public ResponseEntity<String> rechazarRestock(Long idRestock, Long idAprobador) {
        Optional<Restock> solicitudOpt = restockRepository.findById(idRestock);
        if (solicitudOpt.isEmpty()) return ResponseEntity.status(404).body("Lista no encontrada");

        Restock solicitud = solicitudOpt.get();

        if (!solicitud.getEstado().equalsIgnoreCase("PENDIENTE")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La solicitud ya fue procesada");
        }

        solicitud.setIdAprobador(idAprobador);
        solicitud.setEstado("RECHAZADO");
        restockRepository.save(solicitud);

        return ResponseEntity.status(200).body("Solicitud de restock rechazada");
    }


}