package duoc.rocio.inventario.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public boolean crearListaRestock(Long idProveedor, Long idSolicitante) {
        Optional<Proveedor> proveedorEncontrado = proveedorRepository.findById(idProveedor);
        
        if (proveedorEncontrado.isEmpty()) {
            return false;
        }

        Restock nuevaLista = new Restock();
        nuevaLista.setProveedor(proveedorEncontrado.get());
        nuevaLista.setIdSolicitante(idSolicitante);
        nuevaLista.setFechaSolicitud(LocalDate.now());
        nuevaLista.setEstado("PENDIENTE");

        restockRepository.save(nuevaLista);

        return true;
    }

    // AGREGAR PRODUCTO A LA LISTA
    @Transactional
    public int agregarProd(Long idRestock, Long idProdInv, int cant) {
        Optional<Restock> restockBuscado = restockRepository.findById(idRestock);
        if (restockBuscado.isEmpty()) return 1; 
        
        Optional<ProductoInventario> productoBuscado = productoInventarioRepository.findById(idProdInv);
        if (productoBuscado.isEmpty()) return 2;
        
        Restock restock = restockBuscado.get();
        ProductoInventario producto = productoBuscado.get();

        ProdRestock nuevoProdRestock = new ProdRestock();
        nuevoProdRestock.setNombre(producto.getNombreProd()); 
        nuevoProdRestock.setCantidad(cant);
        
        restock.getProductosRestock().add(nuevoProdRestock);
        restockRepository.save(restock);

        return 0;
    }

    // MODIFICAR CANTIDAD DE UN PRODUCTO EN LA LISTA
    @Transactional
    public int modificarCant(Long idRestock, Long idProdRestock, int nuevaCant) {
        
        Optional<Restock> restockBuscado = restockRepository.findById(idRestock);
        
        if (restockBuscado.isEmpty()) return 1;
        
        Restock restock = restockBuscado.get();
        ProdRestock productoEncontrado = null;

        for (ProdRestock p : restock.getProductosRestock()) {
            if (p.getIdProdRestock().equals(idProdRestock)) {
                productoEncontrado = p;
                break;
            }
        }

        if (productoEncontrado == null) return 2;

        productoEncontrado.setCantidad(nuevaCant);
        restockRepository.save(restock);

        return 0;
    }

    @Transactional
    public int eliminarProd(Long idRestock, Long idProdRestock) {
        
        Optional<Restock> restockBuscado = restockRepository.findById(idRestock);
        if (restockBuscado.isEmpty()) return 1;

        Restock restock = restockBuscado.get();
        
        ProdRestock productoAEliminar = null;

        for (ProdRestock p : restock.getProductosRestock()) {
            if (p.getIdProdRestock().equals(idProdRestock)) {
                productoAEliminar = p;
                break;
            }
        }
        if (productoAEliminar == null) return 2;

        restock.getProductosRestock().remove(productoAEliminar);
        restockRepository.save(restock);
        
        return 0;
    }

    // 5. MOSTRAR RESTOCK COMPLETO
    public Optional<Restock> mostrarRestock(Long idRestock) {
        return restockRepository.findById(idRestock);
    }

    // 6. MOSTRAR SOLO LOS PRODUCTOS DE UNA LISTA
    public Optional<List<ProdRestock>> mostrarProductos(Long idRestock) {
        Optional<Restock> solicitud = restockRepository.findById(idRestock);
        
        if (solicitud.isEmpty()) {
            return Optional.empty();
        }
        
        return Optional.of(solicitud.get().getProductosRestock());
    }

    // 7. APROBAR RESTOCK
    @Transactional
    public int aprobarRestock(Long idRestock, Long idAprobador) {
        Optional<Restock> solicitud1 = restockRepository.findById(idRestock);
        if (solicitud1.isEmpty()) return 1;

        Restock solicitud = solicitud1.get();

        if (!solicitud.getEstado().equalsIgnoreCase("PENDIENTE")) {
            return 2;
        }

        for (ProdRestock prodSolicitado : solicitud.getProductosRestock()) {
            Optional<ProductoInventario> prodInvOpt = productoInventarioRepository.findByNombreProd(prodSolicitado.getNombre());
            
            if (prodInvOpt.isPresent()) {
                ProductoInventario producto = prodInvOpt.get();
                producto.setStockActual(producto.getStockActual() + prodSolicitado.getCantidad());
                productoInventarioRepository.save(producto);
            }
        }

        solicitud.setIdAprobador(idAprobador);
        solicitud.setEstado("APROBADO");
        restockRepository.save(solicitud);

        return 0;
    }

    // 8. RECHAZAR RESTOCK
    public int rechazarRestock(Long idRestock, Long idAprobador) {
        Optional<Restock> solicitudOpt = restockRepository.findById(idRestock);
        if (solicitudOpt.isEmpty()) return 1;

        Restock solicitud = solicitudOpt.get();

        if (!solicitud.getEstado().equalsIgnoreCase("PENDIENTE")) {
            return 2;
        }

        solicitud.setIdAprobador(idAprobador);
        solicitud.setEstado("RECHAZADO");
        restockRepository.save(solicitud);

        return 0;
    }


}