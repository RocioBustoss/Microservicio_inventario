package duoc.rocio.inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import duoc.rocio.inventario.dto.InventarioResumenDTO;
import duoc.rocio.inventario.model.Inventario;
import duoc.rocio.inventario.repository.InventarioRepository;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    public List<InventarioResumenDTO> getInventarios() {
        List<Inventario> inventarios = inventarioRepository.findAll();
        
        // EN VEZ DE MOSTRAR TODOS LOS INVENTARIOS (CON SUS PRODUCTOS) SOLO MUESTRA EL ID, NOMBRE Y DESCRIPCION
        return inventarios.stream().map(inventario -> new InventarioResumenDTO(
                inventario.getIdInventario(),
                inventario.getNombreInv(),
                inventario.getDescripcionInv()
            ))
            .toList();
    }
    
    public Optional<Inventario> getInvById(Long idInventario) {
        return inventarioRepository.findById(idInventario);
    }

    public ResponseEntity<String> guardarInv(Inventario invNuevo) {
        inventarioRepository.save(invNuevo);
        return ResponseEntity.status(201).body("Inventario creado correctamente");
    }

    public ResponseEntity<String> eliminarInv(Long idInventario) {
        if (!inventarioRepository.existsById(idInventario)) {
            return ResponseEntity.status(404).body("Inventario no encontrado");
        }
        inventarioRepository.deleteById(idInventario);
        return ResponseEntity.status(200).body("Inventario eliminado correctamente");
    }
}
