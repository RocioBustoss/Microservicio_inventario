package duoc.rocio.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import duoc.rocio.inventario.model.Proveedor;
import java.util.List;


@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long>{
    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByRut(String rut);
}
