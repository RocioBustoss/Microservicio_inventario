package duoc.rocio.inventario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import duoc.rocio.inventario.model.Proveedor;
import duoc.rocio.inventario.repository.ProveedorRepository;

public class ProveedorServiceTest {    
    @Mock
    private ProveedorRepository proveedorRepo;

    @InjectMocks
    private ProveedorService proveedorService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProveedores(){
        // Preparación
        List<Proveedor> listaProveedores = new ArrayList<>();

        Proveedor p1 = new Proveedor(1L, "Distribuidora del Sur", "12.345.678-9", "ventas@sur.cl", "912345678");
        Proveedor p2 = new Proveedor(2L, "Insumos BioBio", "98.765.432-1", "contacto@biobio.cl", "987654321");

        listaProveedores.add(p1);
        listaProveedores.add(p2);

        //Configuración
        when(proveedorRepo.findAll()).thenReturn(listaProveedores);

        // Testeo
        List<Proveedor> resultado = proveedorService.getProveedores();

        // Verificación
        assertEquals(resultado.size(), 2);
        assertEquals(resultado.get(0).getNombre(), "Distribuidora del Sur");
        verify(proveedorRepo, times(1)).findAll();

    }

    @Test
    void testGetProveedores_Vacia(){
        // Preparación
        // Configuración
        when(proveedorRepo.findAll()).thenReturn(new ArrayList<>());

        // Testeo
        List<Proveedor> resultado = proveedorService.getProveedores();

        // Verificación
        assertEquals(resultado.size(), 0);
        verify(proveedorRepo, times(1)).findAll();

    }

    @Test
    void testGetProvById_Encontrado(){
        // Preparación
        Proveedor prov1 = new Proveedor(1L, "Distribuidora del Sur", "12.345.678-9", "ventas@sur.cl", "912345678");
        
        // Configuración
        when(proveedorRepo.findById(1L)).thenReturn(Optional.of(prov1));

        // Testeo
        Optional<Proveedor> resultado = proveedorService.getProvById(1L);

        // Verificación
        assertTrue(resultado.isPresent());
        assertEquals("Distribuidora del Sur", resultado.get().getNombre());
        verify(proveedorRepo, times(1)).findById(1L);
    }

    @Test
    void testGetProvById_NoEncontrado(){
        // Preparación
        // Configuración
        when(proveedorRepo.findById(99L)).thenReturn(Optional.empty());

        // Testeo
        Optional<Proveedor> resultado = proveedorService.getProvById(99L);

        // Verificación
        assertTrue(resultado.isEmpty(), "El resultado debería estar vacío");
        verify(proveedorRepo, times(1)).findById(99L);
    }

    @Test
    void testGuardarProv_Exito(){
        // Preparación
        Proveedor prov = new Proveedor(null, "Distribuidora del Sur", "12.345.678-9", "ventas@sur.cl", "912345678");

        // Configuración
        when(proveedorRepo.existsByRut("12.345.678-9")).thenReturn(false);

        // Testeo
        int resultado = proveedorService.guardarProv(prov);

        // Verificación
        assertEquals(0, resultado);
        verify(proveedorRepo, times(1)).save(prov);
    }

    @Test
    void testGuardarProv_RutDuplicado(){
        // Preparación
        Proveedor prov = new Proveedor(null, "Distribuidora del Sur", "12.345.678-9", "ventas@sur.cl", "912345678");

        // Configuración
        when(proveedorRepo.existsByRut("12.345.678-9")).thenReturn(true);

        // Testeo
        int resultado = proveedorService.guardarProv(prov);

        // Verificación
        assertEquals(1, resultado);
        verify(proveedorRepo, times(0)).save(any(Proveedor.class));
    }
    

    @Test
    void testActualizarProveedor(){
        // Preparación
        Proveedor existente = new Proveedor(1L, "Nombre Viejo", "11.111.111-1", "viejo@mail.com", "123");
        Proveedor nuevosDatos = new Proveedor(null, "Nombre Nuevo", "11.111.111-1", "nuevo@mail.com", "456");

        // Configuración
        when(proveedorRepo.findById(1L)).thenReturn(Optional.of(existente));

        // Testeo
        boolean resultado = proveedorService.actualizarProveedor(1L, nuevosDatos);

        // Verificación
        assertTrue(resultado);
        assertEquals("Nombre Nuevo", existente.getNombre());
        verify(proveedorRepo, times(1)).save(existente);
    }

    @Test
    void testActualizarProveedor_NoEncontrado(){
        Proveedor nuevosDatos = new Proveedor(null, "Nombre", "11.111.111-1", "mail@mail.com", "123");

        // Configuración
        when(proveedorRepo.findById(1L)).thenReturn(Optional.empty());

        // Testeo
        boolean resultado = proveedorService.actualizarProveedor(1L, nuevosDatos);

        // Verificación
        assertFalse(resultado);
        verify(proveedorRepo, times(0)).save(any(Proveedor.class));
    }

    @Test
    void testEliminarProveedor(){
        // Preparación
        // Configuración
        when(proveedorRepo.existsById(1L)).thenReturn(true);
        doNothing().when(proveedorRepo).deleteById(1L);

        // Testeo
        boolean resultado = proveedorService.eliminarProv(1L);

        // Verificación
        assertTrue(resultado);
        verify(proveedorRepo, times(1)).existsById(1L);
        verify(proveedorRepo, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarProveedor_NoEncontrado(){
        // Preparación
        // Configuración
        when(proveedorRepo.existsById(1L)).thenReturn(false);

        // Testeo
        boolean resultado = proveedorService.eliminarProv(1L);

        // Verificación
        assertEquals(resultado, false);
        verify(proveedorRepo, times(1)).existsById(1L);
        verify(proveedorRepo, times(0)).deleteById(1L);
    }
}