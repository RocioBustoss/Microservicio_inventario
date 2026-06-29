package duoc.rocio.inventario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import duoc.rocio.inventario.dto.InventarioResumenDTO;
import duoc.rocio.inventario.model.Inventario;
import duoc.rocio.inventario.model.ProductoInventario;
import duoc.rocio.inventario.repository.InventarioRepository;

public class InventarioServiceTest {
    
    @Mock
    private InventarioRepository inventarioRepo;

    @InjectMocks
    private InventarioService inventarioService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetInventarios(){
        // Preparación  
        List<Inventario> listaInventarios = new ArrayList<>();
        List<ProductoInventario> productos = new ArrayList<>();
        
        Inventario inv1 = new Inventario(1L, "Concepcion", "Inventario para la tienda de Concepción", productos);
        Inventario inv2 = new Inventario(2L, "Talcahuano", "Inventario para la tienda de Talcahuano", productos);
        Inventario inv3 = new Inventario(3L, "Chiguayante", "Inventario para la tienda de Chiguayante", productos);

        listaInventarios.add(inv1);
        listaInventarios.add(inv2);
        listaInventarios.add(inv3);

        // Configuración
        when(inventarioRepo.findAll()).thenReturn(listaInventarios);
        
        // Testeo
        List<InventarioResumenDTO> resultado = inventarioService.getInventarios();
        
        // Verificación
        assertEquals(resultado.size(), 3);
        assertEquals(resultado.get(0).getIdInventario(), 1L);
        assertEquals(resultado.get(1).getIdInventario(), 2L);
        assertEquals(resultado.get(2).getIdInventario(), 3L);

        verify(inventarioRepo, times(1)).findAll();
    }

    @Test
    void testGetInventariosVacio(){
        // Preparación  
        List<Inventario> listaInventarios = new ArrayList<>();
       

        // Configuración
        when(inventarioRepo.findAll()).thenReturn(listaInventarios);
        
        // Testeo
        List<InventarioResumenDTO> resultado = inventarioService.getInventarios();
        
        // Verificación
        assertEquals(resultado.size(), 0);

        verify(inventarioRepo, times(1)).findAll();
    }

    @Test
    void testGetInvById_Existe(){
        // Preparación  
        List<Inventario> listaInventarios = new ArrayList<>();
        List<ProductoInventario> productos = new ArrayList<>();
        
        Inventario inv1 = new Inventario(1L, "Concepcion", "Inventario para la tienda de Concepción", productos);

        listaInventarios.add(inv1);

        // Configuración
        when(inventarioRepo.findById(1L)).thenReturn(Optional.of(inv1));
        
        // Testeo
        Optional<Inventario> resultado = inventarioService.getInvById(1L);
        
        // Verificación
        assertEquals(resultado.isPresent(), true);

        verify(inventarioRepo, times(1)).findById(1L);
    }

        @Test
    void testGetInvById_NoExiste(){
        // Configuración
        when(inventarioRepo.findById(1L)).thenReturn(Optional.empty());
        
        // Testeo
        Optional<Inventario> resultado = inventarioService.getInvById(1L);
        
        // Verificación
        assertEquals(resultado.isPresent(), false);

        verify(inventarioRepo, times(1)).findById(1L);
    }

    @Test
    void testGuardarInv(){
        // Preparación  
        List<ProductoInventario> productos = new ArrayList<>();
        
        Inventario inventario = new Inventario(1L, "Concepcion", "Inventario para la tienda de Concepción", productos);
        Inventario inventarioGuardado = new Inventario(1L, "Concepcion", "Inventario para la tienda de Concepción", productos);

        // Configuracion
        when(inventarioRepo.save(inventario)).thenReturn(inventarioGuardado);

        // Testeo
        Inventario resultado = inventarioService.guardarInv(inventario);

        // Verificación
        assertEquals(resultado, inventarioGuardado);
        assertEquals(resultado.getIdInventario(), 1L);
        assertEquals(resultado.getNombreInv(), "Concepcion");
    }

    @Test
    void testEliminarInv_Existe(){
        // Preparación
        when(inventarioRepo.existsById(1L)).thenReturn(true);
        
        // Configuración
        doNothing().when(inventarioRepo).deleteById(1L);

        // Testeo
        boolean resultado = inventarioService.eliminarInv(1L);

        // Verificación
        assertEquals(resultado, true);
        verify(inventarioRepo, times(1)).existsById(1L);
        verify(inventarioRepo, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarInv_NoExiste(){
        // Preparación
        when(inventarioRepo.existsById(1L)).thenReturn(false);
        
        // Configuración
        doNothing().when(inventarioRepo).deleteById(1L);

        // Testeo
        boolean resultado = inventarioService.eliminarInv(1L);

        // Verificación
        assertEquals(resultado, false);
        verify(inventarioRepo, times(1)).existsById(1L);
        verify(inventarioRepo, times(0)).deleteById(1L);
    }
}