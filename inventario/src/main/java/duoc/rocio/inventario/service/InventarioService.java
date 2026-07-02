package duoc.rocio.inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

    public Inventario guardarInv(Inventario invNuevo) {
        return inventarioRepository.save(invNuevo);
    }

    public boolean eliminarInv(Long idInventario) {
        if (!inventarioRepository.existsById(idInventario)) {
            return false;
        }

        inventarioRepository.deleteById(idInventario);
        return true;
    }
}
