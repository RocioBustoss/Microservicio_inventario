package duoc.rocio.inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import duoc.rocio.inventario.model.Proveedor;
import duoc.rocio.inventario.repository.ProveedorRepository;

@Service
public class ProveedorService {
    @Autowired
    private ProveedorRepository proveedorRepository;
    
    public List<Proveedor> getProveedores(){
        return proveedorRepository.findAll();
    }


    public Optional<Proveedor> getProvById(Long idProveedor){
        return proveedorRepository.findById(idProveedor);
    }


    public int guardarProv(Proveedor proveedorNuevo) {
        if (proveedorRepository.existsByRut(proveedorNuevo.getRut())) {
            return 1;
        }

        proveedorRepository.save(proveedorNuevo);
        return 0;
    }


    public boolean actualizarProveedor(Long idProveedor, Proveedor proveedorActualizado) {
        Optional<Proveedor> proveedorEncontrado = proveedorRepository.findById(idProveedor);

        if (proveedorEncontrado.isEmpty()) {
            return false;
        }

        Proveedor proveedor = proveedorEncontrado.get();
        proveedor.setNombre(proveedorActualizado.getNombre());
        proveedor.setCorreo(proveedorActualizado.getCorreo());
        proveedor.setTelefono(proveedorActualizado.getTelefono());

        proveedorRepository.save(proveedor);

        return true;
    }


    public boolean eliminarProv(Long idProveedor){
        if (!proveedorRepository.existsById(idProveedor)) {
            return false;
        }

        proveedorRepository.deleteById(idProveedor);
        return true;
    }
}

