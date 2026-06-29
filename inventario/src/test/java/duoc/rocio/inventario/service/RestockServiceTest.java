package duoc.rocio.inventario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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

import duoc.rocio.inventario.model.ProdRestock;
import duoc.rocio.inventario.model.ProductoInventario;
import duoc.rocio.inventario.model.Proveedor;
import duoc.rocio.inventario.model.Restock;
import duoc.rocio.inventario.repository.ProductoInventarioRepository;
import duoc.rocio.inventario.repository.ProveedorRepository;
import duoc.rocio.inventario.repository.RestockRepository;

public class RestockServiceTest {
    @Mock
    private ProveedorRepository proveedorRepo;

    @Mock
    private RestockRepository restockRepo;

    @Mock
    private ProductoInventarioRepository productoInventarioRepo;

    @InjectMocks
    private RestockService restockService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearListaRestock_Exito(){
        // Preparación
        Proveedor prov = new Proveedor(1L, "Distribuidora del Sur", "12.345.678-9", "ventas@sur.cl", "912345678");
        
        // Configuración
        when(proveedorRepo.findById(1L)).thenReturn(Optional.of(prov));

        // Testeo
        boolean resultado = restockService.crearListaRestock(1L, 2L);

        // Verificación
        assertTrue(resultado);
        verify(restockRepo, times(1)).save(any(Restock.class));
    }

    @Test
    void testCrearListaRestock_ProveedorNoExiste(){
        // Preparación        
        // Configuración
        when(proveedorRepo.findById(1L)).thenReturn(Optional.empty());

        // Testeo
        boolean resultado = restockService.crearListaRestock(1L, 2L);

        // Verificación
        assertEquals(resultado, false);
        verify(restockRepo, times(0)).save(any(Restock.class));
    }

    @Test
    void testAgregarProd_Exito(){
        // Preparación
        Restock restock = new Restock();
        ProductoInventario prod = new ProductoInventario();
        prod.setNombreProd("Jabón");

        // Configuración
        when(restockRepo.findById(1L)).thenReturn(Optional.of(restock));
        when(productoInventarioRepo.findById(5L)).thenReturn(Optional.of(prod));

        // Testeo
        int resultado = restockService.agregarProd(1L, 5L, 10);

        // Verificación
        assertEquals(0, resultado);
        assertEquals(1, restock.getProductosRestock().size());
        verify(restockRepo, times(1)).save(restock);        
    }

    @Test
    void testAgregarProd_RestockNoEncontrado(){
        // Preparación
        // Configuración
        when(restockRepo.findById(99L)).thenReturn(Optional.empty());

        // Testeo
        int resultado = restockService.agregarProd(1L, 5L, 10);

        // Verificación
        assertEquals(1, resultado);
        verify(restockRepo, times(0)).save(any());
    }

    @Test
    void testAgregarProd_ProductoNoEncontrado(){
        // Preparación
        Restock restock = new Restock();
        
        // Configuración
        when(restockRepo.findById(1L)).thenReturn(Optional.of(restock));
        when(productoInventarioRepo.findById(99L)).thenReturn(Optional.empty());

        // Testeo
        int resultado = restockService.agregarProd(1L, 99L, 10);

        // Verificación
        assertEquals(2, resultado);
        verify(restockRepo, times(0)).save(any());
    }

    @Test
    void testModificarCant_Exito(){
        // Preparación
        List<ProdRestock> listaProdRestocks = new ArrayList<>();

        ProdRestock p1 = new ProdRestock(3L, "Jabón", 5);
        listaProdRestocks.add(p1);

        Restock restock = new Restock();
        restock.setProductosRestock(listaProdRestocks);

        // Configuración
        when(restockRepo.findById(1L)).thenReturn(Optional.of(restock));

        // Testeo
        int resultado = restockService.modificarCant(1L, 3L, 20);

        // Verificación
        assertEquals(0, resultado);
        assertEquals(20, p1.getCantidad());
        verify(restockRepo, times(1)).save(restock);
    }

    @Test
    void testModificarCant_RestockNoExiste(){
        // Preparación
        // Configuración
        when(restockRepo.findById(99L)).thenReturn(Optional.empty());

        // Testeo
        int resultado = restockService.modificarCant(99L, 3L, 20);

        // Verificación
        assertEquals(1, resultado);
        verify(restockRepo, times(0)).save(any());
    }

    @Test
    void testModificarCant_ProdNoEnLista(){
        // Preparación
        List<ProdRestock> listaProdRestocks = new ArrayList<>();
        Restock restock = new Restock();
        restock.setProductosRestock(listaProdRestocks);

        // Configuración
        when(restockRepo.findById(1L)).thenReturn(Optional.of(restock));

        // Testeo
        int resultado = restockService.modificarCant(1L, 1L, 20); 

        // Verificación
        assertEquals(2, resultado);
        verify(restockRepo, times(0)).save(any());
    }

    @Test
    void testModificarCant_ProductoNoEncontradoEnListaConOtros() {
        // 1. Preparación
        ProdRestock p1 = new ProdRestock();
        p1.setIdProdRestock(5L);
        
        Restock restock = new Restock();
        restock.setProductosRestock(new ArrayList<>(List.of(p1)));

        // 2. Configuración
        when(restockRepo.findById(1L)).thenReturn(Optional.of(restock));

        // 3. Testeo
        int resultado = restockService.modificarCant(1L, 99L, 20);

        // 4. Verificación
        assertEquals(2, resultado); 
    }

    @Test
    void testEliminarProd_Exito(){
        // Preparación
        List<ProdRestock> listaProdRestocks = new ArrayList<>();

        ProdRestock p1 = new ProdRestock(3L, "Jabón", 5);
        listaProdRestocks.add(p1);

        Restock restock = new Restock();
        restock.setProductosRestock(listaProdRestocks);

        // Configuración
        when(restockRepo.findById(1L)).thenReturn(Optional.of(restock));

        // Testeo
        int resultado = restockService.eliminarProd(1L, 3L);

        // Verificación
        assertEquals(0, resultado);
        assertEquals(0, restock.getProductosRestock().size(), "La lista debería haber quedado vacía");
        verify(restockRepo, times(1)).save(restock);
    }

    @Test
    void testEliminarProd_RestockNoExiste(){
        // Preparación
        // Configuración
        when(restockRepo.findById(1L)).thenReturn(Optional.empty());

        // Testeo
        int resultado = restockService.eliminarProd(1L, 3L);

        // Verificación
        assertEquals(1, resultado);
        verify(restockRepo, times(0)).save(any());
    }

    @Test
    void testEliminarProd_ProdNoEnLista(){

        // Preparación
        List<ProdRestock> listaProdRestocks = new ArrayList<>();

        Restock restock = new Restock();
        restock.setProductosRestock(listaProdRestocks);
        
        // Configuración
        when(restockRepo.findById(1L)).thenReturn(Optional.of(restock));

        // Testeo
        int resultado = restockService.eliminarProd(1L, 1L);

        // Verificación
        assertEquals(2, resultado);
        verify(restockRepo, times(0)).save(any());
    }

    @Test
    void testEliminarProd_ProductoNoEncontradoEnListaConOtros() {
        // 1. Preparación
        ProdRestock p1 = new ProdRestock();
        p1.setIdProdRestock(5L);
        
        Restock restock = new Restock();
        restock.setProductosRestock(new ArrayList<>(List.of(p1)));

        // 2. Configuración
        when(restockRepo.findById(1L)).thenReturn(Optional.of(restock));

        // 3. Testeo
        int resultado = restockService.eliminarProd(1L, 99L);

        // 4. Verificación
        assertEquals(2, resultado);
    }

    @Test
    void testMostrarRestock_Existe(){
        // Preparación
        Restock restock = new Restock();
        restock.setIdRestock(1L);
        restock.setEstado("PENDIENTE");

        // Configuración
        when(restockRepo.findById(1L)).thenReturn(Optional.of(restock));

        // Testeo
        Optional<Restock> resultado = restockService.mostrarRestock(1L);

        // Verificación
        assertTrue(resultado.isPresent());
        assertEquals("PENDIENTE", resultado.get().getEstado());
        verify(restockRepo, times(1)).findById(1L);
    }

    @Test
    void testMostrarRestock_NoExiste(){
        // Preparación
        // Configuración
        when(restockRepo.findById(1L)).thenReturn(Optional.empty());

        // Testeo
        Optional<Restock> resultado = restockService.mostrarRestock(1L);

        // Verificación
        assertTrue(resultado.isEmpty());
        verify(restockRepo, times(1)).findById(1L);
    }

    @Test
    void testMostrarProductos_Exito(){
        // Preparación
        Restock restock = new Restock();
        restock.setProductosRestock(List.of(new ProdRestock(), new ProdRestock()));

        // Configuración
        when(restockRepo.findById(1L)).thenReturn(Optional.of(restock));

        // Testeo
        Optional<List<ProdRestock>> resultado = restockService.mostrarProductos(1L);

        // Verificación
        assertTrue(resultado.isPresent());
        assertEquals(2, resultado.get().size());
        verify(restockRepo, times(1)).findById(1L);
    }

    @Test
    void testMostrarProductos_NoExiste(){
        // Preparación
        // Configuración
        when(restockRepo.findById(99L)).thenReturn(Optional.empty());

        // Testeo
        Optional<List<ProdRestock>> resultado = restockService.mostrarProductos(99L);

        // Verificación
        assertTrue(resultado.isEmpty());
        verify(restockRepo, times(1)).findById(99L);
    }

    @Test
    void testMostrarProductos_ListaVacia(){
        // Preparación
        Restock restock = new Restock();
        restock.setProductosRestock(new ArrayList<>()); 

        // Configuración
        when(restockRepo.findById(1L)).thenReturn(Optional.of(restock));

        // Testeo
        Optional<List<ProdRestock>> resultado = restockService.mostrarProductos(1L);

        // Verificación
        assertTrue(resultado.isPresent());
        assertTrue(resultado.get().isEmpty());
    }

    @Test
    void testAprobarRestock_Exito(){
        // Preparación
        List<ProdRestock> listaProdRestocks = new ArrayList<>();

        ProdRestock p1 = new ProdRestock(3L, "Jabón", 10);
        listaProdRestocks.add(p1);

        Restock restock = new Restock();
        restock.setEstado("PENDIENTE");
        restock.setProductosRestock(listaProdRestocks);
        
        ProductoInventario prodInv = new ProductoInventario();
        prodInv.setNombreProd("Jabón");
        prodInv.setStockActual(50); // Empieza en 50

        // Configuración
        when(restockRepo.findById(1L)).thenReturn(Optional.of(restock));
        when(productoInventarioRepo.findByNombreProd("Jabón")).thenReturn(Optional.of(prodInv));

        // Testeo
        int resultado = restockService.aprobarRestock(1L, 7L);

        // Verificación
        assertEquals(0, resultado);
        assertEquals(60, prodInv.getStockActual());
        assertEquals("APROBADO", restock.getEstado());
        verify(productoInventarioRepo, times(1)).save(prodInv);
        verify(restockRepo, times(1)).save(restock);
    }
   
    @Test
    void testAprobarRestock_RestockNoExiste(){
        // Configuración
        when(restockRepo.findById(99L)).thenReturn(Optional.empty());
        
        // Testeo
        int resultado = restockService.aprobarRestock(99L, 7L);
        
        // Verificación
        assertEquals(1, resultado);
        verify(restockRepo, times(0)).save(any());
    }
    
    @Test
    void testAprobarRestock_YaProcesada(){
        // Preparación
        Restock restock = new Restock();
        restock.setEstado("APROBADO");
        
        // Configuración
        when(restockRepo.findById(1L)).thenReturn(Optional.of(restock));

        // Testeo
        int resultado = restockService.aprobarRestock(1L, 7L);

        // Verificación
        assertEquals(2, resultado);
        verify(restockRepo, times(0)).save(any());
    }

    @Test
    void testAprobarRestock_ProductoNoExisteEnInventario(){
        // Preparación
        ProdRestock p1 = new ProdRestock();
        p1.setNombre("ProductoInexistente");
        p1.setCantidad(10);
        
        Restock restock = new Restock();
        restock.setEstado("PENDIENTE");
        restock.setProductosRestock(List.of(p1));

        // Configuración
        when(restockRepo.findById(1L)).thenReturn(Optional.of(restock));
        when(productoInventarioRepo.findByNombreProd("ProductoInexistente")).thenReturn(Optional.empty());

        // Testeo
        int resultado = restockService.aprobarRestock(1L, 7L);

        // Verificación
        assertEquals(0, resultado);
        assertEquals("APROBADO", restock.getEstado());
        verify(restockRepo, times(1)).save(restock);
    }

    @Test
    void testRechazarRestock_Exito(){
        // Preparación
        Restock restock = new Restock();
        restock.setEstado("PENDIENTE");

        // Configuración
        when(restockRepo.findById(1L)).thenReturn(Optional.of(restock));

        // Testeo
        int resultado = restockService.rechazarRestock(1L, 7L);

        // Verificación
        assertEquals(0, resultado);
        assertEquals("RECHAZADO", restock.getEstado());
        verify(restockRepo, times(1)).save(restock);
    }

    @Test
    void testRechazarRestock_NoExiste(){
        // Configuración
        when(restockRepo.findById(99L)).thenReturn(Optional.empty());

        // Testeo
        int resultado = restockService.rechazarRestock(99L, 7L);

        // Verificación
        assertEquals(1, resultado);
        verify(restockRepo, times(0)).save(any()); 
    }

    @Test
    void testRechazarRestock_YaProcesada(){
        // Preparación
        Restock restock = new Restock();
        restock.setEstado("APROBADO");
        
        // Configuración
        when(restockRepo.findById(1L)).thenReturn(Optional.of(restock));

        // Testeo
        int resultado = restockService.rechazarRestock(1L, 7L);

        // Verificación
        assertEquals(2, resultado);
        verify(restockRepo, times(0)).save(any());
    }

}
