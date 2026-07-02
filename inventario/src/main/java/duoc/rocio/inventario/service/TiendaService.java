package duoc.rocio.inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import duoc.rocio.inventario.model.Tienda;
import duoc.rocio.inventario.repository.TiendaRepository;

@Service
public class TiendaService {

    @Autowired
    private TiendaRepository tiendaRepository;

    public List<Tienda> getTiendas() {
        return tiendaRepository.findAll();
    }
    
    public Optional<Tienda> getTieById(Long idTienda) {
        return tiendaRepository.findById(idTienda);
    }

    public Tienda guardarTie(Tienda tieNueva) {
        return tiendaRepository.save(tieNueva);
    }

    public boolean actualizarTie(Long idTienda, Tienda tiendaActualizada) {
        Optional<Tienda> tiendaEncontrada = tiendaRepository.findById(idTienda);

        if (tiendaEncontrada.isEmpty()) {
            return false;
        }

        Tienda tienda = tiendaEncontrada.get();
        tienda.setNombreTie(tiendaActualizada.getNombreTie());
        tienda.setDescripcionTie(tiendaActualizada.getDescripcionTie());
        tienda.setHorarioTie(tiendaActualizada.getHorarioTie());
        tienda.setPoliticas(tiendaActualizada.getPoliticas());
        

        tiendaRepository.save(tienda);

        return true;
    }

    public boolean eliminarTie(Long idTienda) {
        if (!tiendaRepository.existsById(idTienda)) {
            return false;
        }

        tiendaRepository.deleteById(idTienda);
        return true;
    }
}
