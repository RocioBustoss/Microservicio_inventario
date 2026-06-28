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


    public ResponseEntity<String> guardarProv(Proveedor proveedorNuevo){
        if (proveedorRepository.existsByRut(proveedorNuevo.getRut())){
            return ResponseEntity.status(409).body("El proveedor ya se encuentra registrado");
        }

        proveedorRepository.save(proveedorNuevo);
        return ResponseEntity.status(201).body("Proveedor registrado correctamente");
    }


    public ResponseEntity<String> actualizarProveedor(Long idProveedor, Proveedor proveedorActualizado) {
        Optional<Proveedor> proveedorEncontrado = proveedorRepository.findById(idProveedor);

        if (proveedorEncontrado.isEmpty()) {
            return ResponseEntity.status(404).body("Proveedor no encontrado");
        }

        Proveedor proveedor = proveedorEncontrado.get();
        proveedor.setNombre(proveedorActualizado.getNombre());
        proveedor.setCorreo(proveedorActualizado.getCorreo());
        proveedor.setTelefono(proveedorActualizado.getTelefono());

        proveedorRepository.save(proveedor);

        return ResponseEntity.status(200).body("Proveedor actualizado correctamente");
    }


    public ResponseEntity<String> eliminarProv(Long idProveedor){
        if (!proveedorRepository.existsById(idProveedor)) {
            return ResponseEntity.status(404).body("Proveedor no encontrado");
        }

        proveedorRepository.deleteById(idProveedor);
        return ResponseEntity.status(200).body("Proveedor eliminado correctamente");
    }
}

