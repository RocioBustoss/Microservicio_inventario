package duoc.rocio.inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import duoc.rocio.inventario.model.Inventario;
import duoc.rocio.inventario.repository.InventarioRepository;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    public List<Inventario> obtenerInventarios() {
        return inventarioRepository.findAll();
    }

    public Optional<Inventario> obtenerInventarioPorId(Long idInventario) {
        return inventarioRepository.findById(idInventario);
    }

    public List<Inventario> obtenerInventarioPorTienda(Long idTienda) {
        return inventarioRepository.findByIdTienda(idTienda);
    }

    public ResponseEntity<String> guardarInventario(Inventario inventarioNuevo) {
        if (inventarioRepository.existsByIdTienda(inventarioNuevo.getIdTienda())) {
            return ResponseEntity.status(409).body("La tienda ya tiene un inventario registrado");
        }

        inventarioRepository.save(inventarioNuevo);
        return ResponseEntity.status(201).body("Inventario creado correctamente");
    }

    public ResponseEntity<String> eliminarInventario(Long idInventario) {
        if (!inventarioRepository.existsById(idInventario)) {
            return ResponseEntity.status(404).body("Inventario no encontrado");
        }

        inventarioRepository.deleteById(idInventario);
        return ResponseEntity.status(200).body("Inventario eliminado correctamente");
    }
}
