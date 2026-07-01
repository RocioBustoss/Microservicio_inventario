package duoc.rocio.inventario.controller;

import duoc.rocio.inventario.model.ProdRestock;
import duoc.rocio.inventario.model.Restock;
import duoc.rocio.inventario.service.RestockService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestockController.class)
@ActiveProfiles("test")
public class RestockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestockService restockService;

    @Test
    void crearListaRestock_Exito() throws Exception {
        Mockito.when(restockService.crearListaRestock(1L, 2L)).thenReturn(true);

        mockMvc.perform(post("/api/ecomarket/v1/restocks/proveedor/1/solicitante/2"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Lista de restock creada."));
    }

    @Test
    void crearListaRestock_ProveedorNoEncontrado() throws Exception {
        Mockito.when(restockService.crearListaRestock(1L, 2L)).thenReturn(false);

        mockMvc.perform(post("/api/ecomarket/v1/restocks/proveedor/1/solicitante/2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Proveedor no encontrado"));
    }

    @Test
    void agregarProd_ListaNoEncontrada() throws Exception {
        Mockito.when(restockService.agregarProd(1L, 5L, 10)).thenReturn(1);

        mockMvc.perform(post("/api/ecomarket/v1/restocks/1/productos/5").param("cant", "10"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lista de restock no encontrada"));
    }

    @Test
    void agregarProd_ProductoNoEncontrado() throws Exception {
        Mockito.when(restockService.agregarProd(1L, 5L, 10)).thenReturn(2);

        mockMvc.perform(post("/api/ecomarket/v1/restocks/1/productos/5").param("cant", "10"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Producto no encontrado en inventario"));
    }

    @Test
    void agregarProd_Exito() throws Exception {
        Mockito.when(restockService.agregarProd(1L, 5L, 10)).thenReturn(0);

        mockMvc.perform(post("/api/ecomarket/v1/restocks/1/productos/5").param("cant", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string("Producto agregado a la lista de restock"));
    }


    @Test
    void modificarCant_ListaNoEncontrada() throws Exception {
        Mockito.when(restockService.modificarCant(1L, 3L, 15)).thenReturn(1);

        mockMvc.perform(put("/api/ecomarket/v1/restocks/1/productos/3").param("cant", "15"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lista no encontrada"));
    }

    @Test
    void modificarCant_ProductoNoEncontrado() throws Exception {
        Mockito.when(restockService.modificarCant(1L, 3L, 15)).thenReturn(2);

        mockMvc.perform(put("/api/ecomarket/v1/restocks/1/productos/3").param("cant", "15"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("El producto no está en esta lista"));
    }

    @Test
    void modificarCant_Exito() throws Exception {
        Mockito.when(restockService.modificarCant(1L, 3L, 15)).thenReturn(0);

        mockMvc.perform(put("/api/ecomarket/v1/restocks/1/productos/3").param("cant", "15"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cantidad modificada correctamente"));
    }

    @Test
    void eliminarProd_ListaNoEncontrada() throws Exception {
        Mockito.when(restockService.eliminarProd(1L, 3L)).thenReturn(1);

        mockMvc.perform(delete("/api/ecomarket/v1/restocks/1/productos/3"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lista no encontrada"));
    }

    @Test
    void eliminarProd_ProductoNoEncontrado() throws Exception {
        Mockito.when(restockService.eliminarProd(1L, 3L)).thenReturn(2);

        mockMvc.perform(delete("/api/ecomarket/v1/restocks/1/productos/3"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("El producto no se encontró en la lista"));
    }

    @Test
    void eliminarProd_Exito() throws Exception {
        Mockito.when(restockService.eliminarProd(1L, 3L)).thenReturn(0);

        mockMvc.perform(delete("/api/ecomarket/v1/restocks/1/productos/3"))
                .andExpect(status().isOk())
                .andExpect(content().string("Producto removido de la lista"));
    }


    @Test
    void mostrarRestock_Existe() throws Exception {
        Restock restockMock = new Restock();
        restockMock.setEstado("PENDIENTE");

        Mockito.when(restockService.mostrarRestock(1L)).thenReturn(Optional.of(restockMock));

        mockMvc.perform(get("/api/ecomarket/v1/restocks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado", is("PENDIENTE")));
    }

    @Test
    void mostrarRestock_NoExiste() throws Exception {
        Mockito.when(restockService.mostrarRestock(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/ecomarket/v1/restocks/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lista no encontrada"));
    }

    @Test
    void mostrarProductos_Existe() throws Exception {
        List<ProdRestock> productos = new ArrayList<>();
        ProdRestock p1 = new ProdRestock();
        p1.setNombre("Jabon");
        productos.add(p1);

        Mockito.when(restockService.mostrarProductos(1L)).thenReturn(Optional.of(productos));

        mockMvc.perform(get("/api/ecomarket/v1/restocks/1/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Jabon")));
    }

    @Test
    void mostrarProductos_NoExiste() throws Exception {
        Mockito.when(restockService.mostrarProductos(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/ecomarket/v1/restocks/1/productos"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lista no encontrada"));
    }


    @Test
    void aprobarRestock_ListaNoEncontrada() throws Exception {
        Mockito.when(restockService.aprobarRestock(1L, 7L)).thenReturn(1);

        mockMvc.perform(put("/api/ecomarket/v1/restocks/1/aprobar").param("idAprobador", "7"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lista no encontrada"));
    }

    @Test
    void aprobarRestock_YaProcesada() throws Exception {
        Mockito.when(restockService.aprobarRestock(1L, 7L)).thenReturn(2);

        mockMvc.perform(put("/api/ecomarket/v1/restocks/1/aprobar").param("idAprobador", "7"))
                .andExpect(status().isConflict())
                .andExpect(content().string("La solicitud ya fue procesada"));
    }

    @Test
    void aprobarRestock_Exito() throws Exception {
        Mockito.when(restockService.aprobarRestock(1L, 7L)).thenReturn(0);

        mockMvc.perform(put("/api/ecomarket/v1/restocks/1/aprobar").param("idAprobador", "7"))
                .andExpect(status().isOk())
                .andExpect(content().string("Solicitud aprobada y stock actualizado correctamente"));
    }

    @Test
    void rechazarRestock_ListaNoEncontrada() throws Exception {
        Mockito.when(restockService.rechazarRestock(1L, 7L)).thenReturn(1);

        mockMvc.perform(put("/api/ecomarket/v1/restocks/1/rechazar").param("idAprobador", "7"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lista no encontrada"));
    }

    @Test
    void rechazarRestock_YaProcesada() throws Exception {
        Mockito.when(restockService.rechazarRestock(1L, 7L)).thenReturn(2);

        mockMvc.perform(put("/api/ecomarket/v1/restocks/1/rechazar").param("idAprobador", "7"))
                .andExpect(status().isConflict())
                .andExpect(content().string("La solicitud ya fue procesada"));
    }

    @Test
    void rechazarRestock_Exito() throws Exception {
        Mockito.when(restockService.rechazarRestock(1L, 7L)).thenReturn(0);

        mockMvc.perform(put("/api/ecomarket/v1/restocks/1/rechazar").param("idAprobador", "7"))
                .andExpect(status().isOk())
                .andExpect(content().string("Solicitud de restock rechazada correctamente"));
    }
}