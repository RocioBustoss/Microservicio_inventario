package duoc.rocio.inventario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import duoc.rocio.inventario.model.Tienda;
import duoc.rocio.inventario.repository.TiendaRepository;

public class TiendaServiceTest {
    
    @Mock
    private TiendaRepository tiendaRepository;

    @InjectMocks
    private TiendaService tiendaService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTiendas(){
        // Preparación  
        List<Tienda> listaTiendas = new ArrayList<>();
        
        Tienda inv1 = new Tienda(1L, "Concepcion", "Tienda para la tienda de Concepción", "8:00-18:00", "Politicas de la empresa");
        Tienda inv2 = new Tienda(2L, "Talcahuano", "Tienda para la tienda de Talcahuano", "8:00-18:00", "Politicas de la empresa");
        Tienda inv3 = new Tienda(3L, "Chiguayante", "Tienda para la tienda de Chiguyante", "8:00-18:00", "Politicas de la empresa");

        listaTiendas.add(inv1);
        listaTiendas.add(inv2);
        listaTiendas.add(inv3);

        // Configuración
        when(tiendaRepository.findAll()).thenReturn(listaTiendas);
        
        // Testeo
        List<Tienda> resultado = tiendaService.getTiendas();
        
        // Verificación
        assertEquals(resultado.size(), 3);
        assertEquals(resultado.get(0).getIdTienda(), 1L);
        assertEquals(resultado.get(1).getIdTienda(), 2L);
        assertEquals(resultado.get(2).getIdTienda(), 3L);

        verify(tiendaRepository, times(1)).findAll();
    }

    @Test
    void testGetTiendasVacio(){
        // Preparación  
        List<Tienda> listaTiendas = new ArrayList<>();
       

        // Configuración
        when(tiendaRepository.findAll()).thenReturn(listaTiendas);
        
        // Testeo
        List<Tienda> resultado = tiendaService.getTiendas();
        
        // Verificación
        assertEquals(resultado.size(), 0);

        verify(tiendaRepository, times(1)).findAll();
    }

    @Test
    void testGetTieById_Existe(){
        // Preparación  
        List<Tienda> listaTiendas = new ArrayList<>();
        
        Tienda inv1 = new Tienda(1L, "Concepcion", "Tienda para la tienda de Concepción", "8:00-18:00", "Politicas de la empresa");

        listaTiendas.add(inv1);

        // Configuración
        when(tiendaRepository.findById(1L)).thenReturn(Optional.of(inv1));
        
        // Testeo
        Optional<Tienda> resultado = tiendaService.getTieById(1L);
        
        // Verificación
        assertEquals(resultado.isPresent(), true);

        verify(tiendaRepository, times(1)).findById(1L);
    }

        @Test
    void testGetTieById_NoExiste(){
        // Configuración
        when(tiendaRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Testeo
        Optional<Tienda> resultado = tiendaService.getTieById(1L);
        
        // Verificación
        assertEquals(resultado.isPresent(), false);

        verify(tiendaRepository, times(1)).findById(1L);
    }

    @Test
    void testGuardarInv(){
        // Preparación  
        
        Tienda tie1 = new Tienda(1L, "Concepcion", "Tienda para la tienda de Concepción", "8:00-18:00", "Politicas de la empresa");
        Tienda tieGuardada = new Tienda(1L, "Concepcion", "Tienda para la tienda de Concepción", "8:00-18:00", "Politicas de la empresa");

        // Configuracion
        when(tiendaRepository.save(tie1)).thenReturn(tieGuardada);

        // Testeo
        Tienda resultado = tiendaService.guardarTie(tie1);

        // Verificación
        assertEquals(resultado, tieGuardada);
        assertEquals(resultado.getIdTienda(), 1L);
        assertEquals(resultado.getNombreTie(), "Concepcion");
    }

    @Test
    void testEliminarInv_Existe(){
        // Preparación
        when(tiendaRepository.existsById(1L)).thenReturn(true);
        
        // Configuración
        doNothing().when(tiendaRepository).deleteById(1L);

        // Testeo
        boolean resultado = tiendaService.eliminarTie(1L);

        // Verificación
        assertEquals(resultado, true);
        verify(tiendaRepository, times(1)).existsById(1L);
        verify(tiendaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarInv_NoExiste(){
        // Preparación
        when(tiendaRepository.existsById(1L)).thenReturn(false);
        
        // Configuración
        doNothing().when(tiendaRepository).deleteById(1L);

        // Testeo
        boolean resultado = tiendaService.eliminarTie(1L);

        // Verificación
        assertEquals(resultado, false);
        verify(tiendaRepository, times(1)).existsById(1L);
        verify(tiendaRepository, times(0)).deleteById(1L);
    }

@Test
void testActualizarTienda(){
    // Preparación
    Tienda existente = new Tienda(1L, "Concepcion", "Desc original", "8:00-18:00", "Politicas original");
    Tienda nuevosDatos = new Tienda(1L, "Concepcion", "Nueva Desc", "7:00-20:00", "Politicas");

    // Configuración
    when(tiendaRepository.findById(1L)).thenReturn(Optional.of(existente));

    // Testeo
    boolean resultado = tiendaService.actualizarTie(1L, nuevosDatos);

    // Verificación
    assertTrue(resultado);
    assertEquals("7:00-20:00", existente.getHorarioTie());
    assertEquals("Politicas", existente.getPoliticas());
    
    // Verificamos que el repositorio efectivamente guardó el cambio
    verify(tiendaRepository, times(1)).save(existente);
}

@Test
void testActualizarTienda_NoExiste() {
    // Preparación
    Tienda nuevosDatos = new Tienda(1L, "Nombre", "Desc", "8:00-18:00", "Politicas");
    
    // Configuración
    when(tiendaRepository.findById(1L)).thenReturn(Optional.empty());

    // Testeo
    boolean resultado = tiendaService.actualizarTie(1L, nuevosDatos);

    // Verificación
    assertEquals(false, resultado);
    verify(tiendaRepository, times(0)).save(any(Tienda.class));
}

}