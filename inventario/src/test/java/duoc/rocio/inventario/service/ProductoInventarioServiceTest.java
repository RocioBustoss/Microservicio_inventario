package duoc.rocio.inventario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import duoc.rocio.inventario.model.Inventario;
import duoc.rocio.inventario.model.ProductoInventario;
import duoc.rocio.inventario.repository.InventarioRepository;
import duoc.rocio.inventario.repository.ProductoInventarioRepository;


public class ProductoInventarioServiceTest {    
    @Mock
    private InventarioRepository inventarioRepo;
    
    @Mock
    private ProductoInventarioRepository productoInventarioRepo;

    @InjectMocks
    private ProductoInventarioService productoInventarioService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAgregarProd(){
        // Preparación    
        Inventario inv1 = new Inventario(1L, "Concepcion", "Inventario para la tienda de Concepción", null);
        ProductoInventario prodNuevo = new ProductoInventario(1L, "Jabón", "ASEO-001", LocalDate.of(2027, 12, 31), 15, null);

        // Configuración
        when(inventarioRepo.findById(1L)).thenReturn(Optional.of(inv1));
        when(productoInventarioRepo.existsByInventario_IdInventarioAndCodigoSku(1L, "ASEO-001")).thenReturn(false);
        
        // Testeo
        int resultado = productoInventarioService.agregarProd(1L, prodNuevo);
        
        // Verificación
        assertEquals(0, resultado);
        verify(productoInventarioRepo, times(1)).save(prodNuevo);
        
    }

    @Test
    void testAgregarProd_InvNoExiste(){
        // Preparación    
        ProductoInventario prodNuevo = new ProductoInventario(1L, "Jabón", "ASEO-001", LocalDate.of(2027, 12, 31), 15, null);

        // Configuración
        when(inventarioRepo.findById(1L)).thenReturn(Optional.empty());
        
        // Testeo
        int resultado = productoInventarioService.agregarProd(1L, prodNuevo);
        
        // Verificación
        assertEquals(1, resultado);
        verify(productoInventarioRepo, times(0)).save(prodNuevo);
    }

    @Test
    void testAgregarProd_ProdYaExiste(){
        // Preparación    
        Inventario inv1 = new Inventario(1L, "Concepcion", "Inventario para la tienda de Concepción", null);
        ProductoInventario prodNuevo = new ProductoInventario(1L, "Jabón", "ASEO-001", LocalDate.of(2027, 12, 31), 15, null);

        // Configuración
        when(inventarioRepo.findById(1L)).thenReturn(Optional.of(inv1));
        when(productoInventarioRepo.existsByInventario_IdInventarioAndCodigoSku(1L, "ASEO-001")).thenReturn(true);
        
        // Testeo
        int resultado = productoInventarioService.agregarProd(1L, prodNuevo);
        
        // Verificación
        assertEquals(2, resultado);
        verify(productoInventarioRepo, times(0)).save(prodNuevo);
    }

    @Test
    void testGetProductosByInv(){
        // Preparación
        List<ProductoInventario> productos = new ArrayList<>();
        Inventario inv1 = new Inventario(1L, "Concepcion", "Inventario para la tienda de Concepción", productos);
        
        ProductoInventario prod1 = new ProductoInventario(1L, "Jabón", "ASEO-001", LocalDate.of(2027, 12, 31), 15, inv1);
        ProductoInventario prod2 = new ProductoInventario(2L, "Cepillo", "ASEO-002", LocalDate.of(2029, 12, 31), 15, inv1);
        ProductoInventario prod3 = new ProductoInventario(3L, "Toalla", "ASEO-003", LocalDate.of(2059, 12, 31), 15, inv1);

        productos.add(prod1);
        productos.add(prod2);
        productos.add(prod3);

        // Configuración
        when(inventarioRepo.findById(1L)).thenReturn(Optional.of(inv1));

        // Testeo
        List<ProductoInventario> resultado = productoInventarioService.getProductosByInv(1L);

        // Verificación
        assertNotNull(resultado);
        assertEquals(resultado.size(), 3);

        verify(inventarioRepo, times(1)).findById(1L);
    }

    @Test
    void testGetProductosByInv_InvVacio(){
        // Peparación
        // Configuración
        when(inventarioRepo.findById(99L)).thenReturn(Optional.empty());

        // Testeo
        List<ProductoInventario> resultado = productoInventarioService.getProductosByInv(1L);

        // Verificación
        assertEquals(resultado.size(), 0);
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        
        verify(inventarioRepo, times(1)).findById(1L);
    }

    @Test
    void testGetProdById(){
        // Preparación
        ProductoInventario prod1 = new ProductoInventario(1L, "Jabón", "ASEO-001", LocalDate.of(2027, 12, 31), 15, null);
        
        // Configuración
        when(productoInventarioRepo.findById(1L)).thenReturn(Optional.of(prod1));

        // Testeo
        Optional<ProductoInventario> resultado = productoInventarioService.getProdById(1L);

        // Verificación
        assertTrue(resultado.isPresent());
        assertEquals(resultado.get().getNombreProd(), "Jabón");

        verify(productoInventarioRepo, times(1)).findById(1L);

    }

    @Test
    void testGetProdById_NoExiste(){
        // Preparación        
        // Configuración
        when(productoInventarioRepo.findById(1L)).thenReturn(Optional.empty());

        // Testeo
        Optional<ProductoInventario> resultado = productoInventarioService.getProdById(1L);

        // Verificación
        assertTrue(resultado.isEmpty());
        verify(productoInventarioRepo, times(1)).findById(1L);

    }

    @Test
    void testGetProductoByInvAndId(){
        // Preparación
        List<ProductoInventario> productos = new ArrayList<>();
        Inventario inv1 = new Inventario(1L, "Concepcion", "Inventario para la tienda de Concepción", productos);
        
        ProductoInventario prod1 = new ProductoInventario(1L, "Jabón", "ASEO-001", LocalDate.of(2027, 12, 31), 15, inv1);
        
        productos.add(prod1);

        // Configuración
        when(productoInventarioRepo.findByIdProductoAndInventario_IdInventario(1L, 1L)).thenReturn(Optional.of(prod1));
        
        // Testeo
        Optional<ProductoInventario> resultado = productoInventarioService.getProductoByInvAndId(1L, 1L);
        
        // Verificación
        assertTrue(resultado.isPresent());
        assertEquals(resultado.get().getNombreProd(), "Jabón");

        verify(productoInventarioRepo, times(1)).findByIdProductoAndInventario_IdInventario(1L, 1L);
    }

    @Test
    void testGetProductoByInvAndId_ProdNoExiste(){
        // Preparación
        
        // Configuración
        when(productoInventarioRepo.findByIdProductoAndInventario_IdInventario(1L, 1L)).thenReturn(Optional.empty());
        
        // Testeo
        Optional<ProductoInventario> resultado = productoInventarioService.getProductoByInvAndId(1L, 1L);
        
        // Verificación
        assertTrue(resultado.isEmpty());

        verify(productoInventarioRepo, times(1)).findByIdProductoAndInventario_IdInventario(1L, 1L);
    }
    @Test
    void testConsultarStock(){
        // Preparación
        ProductoInventario prod1 = new ProductoInventario(1L, "Jabón", "ASEO-001", LocalDate.of(2027, 12, 31), 15, null);
        
        // Configuración
        when(productoInventarioRepo.findByIdProductoAndInventario_IdInventario(1L, 1L)).thenReturn(Optional.of(prod1));

        // Testeo
        Optional<Integer> resultado = productoInventarioService.consultarStock(1L, 1L);

        // Verificación
        assertTrue(resultado.isPresent());
        assertEquals(resultado.get(), 15);

        verify(productoInventarioRepo, times(1)).findByIdProductoAndInventario_IdInventario(1L, 1L);

    }

    @Test
    void testConsultarStock_ProdNoEncontrado(){
        // Preparación
        
        // Configuración
        when(productoInventarioRepo.findByIdProductoAndInventario_IdInventario(1L, 1L)).thenReturn(Optional.empty());

        // Testeo
        Optional<Integer> resultado = productoInventarioService.consultarStock(1L, 1L);

        // Verificación
        assertTrue(resultado.isEmpty());
        verify(productoInventarioRepo, times(1)).findByIdProductoAndInventario_IdInventario(1L, 1L);


    }
    
    @Test
    void testActualizarStock(){
        // Preparación
        ProductoInventario prod1 = new ProductoInventario(1L, "Jabón", "ASEO-001", LocalDate.of(2027, 12, 31), 15, null);

        // Configuración
        when(productoInventarioRepo.findByIdProductoAndInventario_IdInventario(1L, 1L)).thenReturn(Optional.of(prod1));

        // Testeo
        int resultado = productoInventarioService.actualizarStock(1L,1L, 10);

        // Verificación
        assertEquals(resultado, 0);
    }

    @Test
    void testActualizarStock_ProdNoEncontrado(){
        // Preparación

        // Configuración
        when(productoInventarioRepo.findByIdProductoAndInventario_IdInventario(1L, 1L)).thenReturn(Optional.empty());

        // Testeo
        int resultado = productoInventarioService.actualizarStock(1L,1L, 10);

        // Verificación
        assertEquals(resultado, 1);
    }

    @Test
    void testActualizarStock_CantidadNegativa(){
        // Preparación
        ProductoInventario prod1 = new ProductoInventario(1L, "Jabón", "ASEO-001", LocalDate.of(2027, 12, 31), 15, null);

        // Configuración
        when(productoInventarioRepo.findByIdProductoAndInventario_IdInventario(1L, 1L)).thenReturn(Optional.of(prod1));

        // Testeo
        int resultado = productoInventarioService.actualizarStock(1L,1L, -5);

        // Verificación
        assertEquals(resultado, 2);
    }

    @Test
    void testActualizarStock_CantidadSinCambio(){
        // Preparación
        ProductoInventario prod1 = new ProductoInventario(1L, "Jabón", "ASEO-001", LocalDate.of(2027, 12, 31), 15, null);

        // Configuración
        when(productoInventarioRepo.findByIdProductoAndInventario_IdInventario(1L, 1L)).thenReturn(Optional.of(prod1));

        // Testeo
        int resultado = productoInventarioService.actualizarStock(1L,1L, 0);

        // Verificación
        assertEquals(resultado, 3);
    }

    @Test
    void testFindByNombre_Encontrado(){
        // Preparación
        List<ProductoInventario> productos = new ArrayList<>();
        Inventario inv1 = new Inventario(1L, "Concepcion", "Inventario para la tienda de Concepción", productos);
        
        ProductoInventario prod1 = new ProductoInventario(1L, "Jabón", "ASEO-001", LocalDate.of(2027, 12, 31), 15, inv1);
        
        productos.add(prod1);

        // Configuración
        when(productoInventarioRepo.findByInventario_IdInventarioAndNombreProdContainingIgnoreCase(1L, "Jabón")).thenReturn(productos);

        // Testeo
        List<ProductoInventario> resultado = productoInventarioService.findByNombre(1L, "Jabón");

        // Verificación
        assertEquals(resultado.size(), 1);
        assertEquals(resultado.get(0).getNombreProd(), "Jabón" );
        verify(productoInventarioRepo, times(1)).findByInventario_IdInventarioAndNombreProdContainingIgnoreCase(1L, "Jabón");
    }

    @Test
    void testFindByNombre_Vacio(){
        // Preparación
        // Configuración
        when(productoInventarioRepo.findByInventario_IdInventarioAndNombreProdContainingIgnoreCase(1L, "Jabón")).thenReturn(new ArrayList<>());

        // Testeo
        List<ProductoInventario> resultado = productoInventarioService.findByNombre(1L, "Jabón");

        // Verificación
        assertTrue(resultado.isEmpty());
        verify(productoInventarioRepo, times(1)).findByInventario_IdInventarioAndNombreProdContainingIgnoreCase(1L, "Jabón");
    }

    @Test
    void testBuscarStockBajo(){
        // Preparación
        List<ProductoInventario> productos = new ArrayList<>();
        Inventario inv1 = new Inventario(1L, "Concepcion", "Inventario para la tienda de Concepción", productos);
        
        ProductoInventario prod1 = new ProductoInventario(1L, "Jabón", "ASEO-001", LocalDate.of(2027, 12, 31), 2, inv1);
        ProductoInventario prod2 = new ProductoInventario(2L, "Cepillo", "ASEO-002", LocalDate.of(2029, 12, 31), 3, inv1);

        productos.add(prod1);
        productos.add(prod2);


        // Configuración
        when(productoInventarioRepo.findByInventario_IdInventarioAndStockActualLessThanEqual(1L, 5)).thenReturn(productos);
        
        // Testeo
        List<ProductoInventario> resultado = productoInventarioService.buscarStockBajo(1L, 5);

        // Verificación
        assertEquals(resultado.size(), 2);
        assertEquals(resultado.get(0).getStockActual(), 2);
        assertEquals(resultado.get(1).getStockActual(), 3);

        verify(productoInventarioRepo, times(1)).findByInventario_IdInventarioAndStockActualLessThanEqual(1L, 5);
    }

    @Test
    void testBuscarStockBajo_Vacio(){
        // Preparación
        List<ProductoInventario> productos = new ArrayList<>();

        // Configuración
        when(productoInventarioRepo.findByInventario_IdInventarioAndStockActualLessThanEqual(1L, 5)).thenReturn(productos);
        
        // Testeo
        List<ProductoInventario> resultado = productoInventarioService.buscarStockBajo(1L, 0);

        // Verificación
        assertTrue(resultado.isEmpty());
        verify(productoInventarioRepo, times(1)).findByInventario_IdInventarioAndStockActualLessThanEqual(1L, 0);
    }

    @Test
    void testEliminarProducto(){
        // Preparación
        // Configuración
        when(productoInventarioRepo.existsById(1L)).thenReturn(true);
        doNothing().when(productoInventarioRepo).deleteById(1L);

        // Testeo
        boolean resultado = productoInventarioService.eliminarProducto(1L);

        // Verificación
        assertTrue(resultado);
        verify(productoInventarioRepo, times(1)).existsById(1L);
        verify(productoInventarioRepo, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarProducto_NoExiste(){
        // Preparación
        // Configuración
        when(productoInventarioRepo.existsById(1L)).thenReturn(false);

        // Testeo
        boolean resultado = productoInventarioService.eliminarProducto(1L);

        // Verificación
        assertEquals(resultado, false);
        verify(productoInventarioRepo, times(1)).existsById(1L);
        verify(productoInventarioRepo, times(0)).deleteById(1L);
    }
}
