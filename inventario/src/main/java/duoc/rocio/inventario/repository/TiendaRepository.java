package duoc.rocio.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import duoc.rocio.inventario.model.Tienda;

@Repository
public interface TiendaRepository extends JpaRepository<Tienda, Long>{
     
}

